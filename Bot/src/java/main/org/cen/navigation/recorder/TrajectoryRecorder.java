package org.cen.navigation.recorder;

import org.cen.geom.Point2D;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.cen.com.IComService;
import org.cen.com.in.InData;
import org.cen.com.in.InDataListener;
import org.cen.logging.LoggingUtils;
import org.cen.navigation.WheelPosition;
import org.cen.robot.attributes.IMotorProperties;
import org.cen.robot.attributes.IRobotDimension;
import org.cen.robot.device.navigation.com.MoveOutData;
import org.cen.robot.device.navigation.com.StopOutData;
import org.cen.robot.device.navigation.position.com.ReadPositionPulseInData;
import org.cen.robot.device.navigation.position.com.ReadPositionPulseOutData;
import org.cen.robot.services.IRobotServiceInitializable;
import org.cen.robot.services.IRobotServiceProvider;
import org.cen.robot.utils.RobotUtils;

public class TrajectoryRecorder implements ITrajectoryRecorder, IRobotServiceInitializable, Runnable, InDataListener {

    private static final Logger LOGGER = LoggingUtils.getClassLogger();

    private static final int MOVE_NONE = 0;

    private static final int MOVE_ROTATION = 1;

    private static final int MOVE_STRAIGHT = 2;

    private static final int PAUSE = 500;

    private static final long TIMEOUT = 1000;

    public static void main(String[] args) {
        TrajectoryRecorder r = new TrajectoryRecorder();
        r.setDebug(true);
        List<MoveOutData> data = r.getMoveData();
        for (MoveOutData d : data) {
            System.out.println(d);
        }
        r.getTrajectory();
    }

    private IComService comService;

    private TrajectoryRecord current;

    private boolean debug = false;

    private boolean paused = true;

    private List<WheelPosition> positions;

    private List<TrajectoryRecord> records;

    private boolean running = true;

    private IRobotServiceProvider servicesProvider;

    private Thread thread;

    public TrajectoryRecorder() {
        super();
        positions = new ArrayList<WheelPosition>();
        records = new ArrayList<TrajectoryRecord>();
    }

    @Override
    public void afterRegister() {
        comService = servicesProvider.getService(IComService.class);
        running = true;
        thread = new Thread(this, getClass().getSimpleName());
        thread.start();
        LOGGER.config("trajectory recorder initialized");
    }

    private List<MoveOutData> builMoveData() {
        ArrayList<MoveOutData> moveData = new ArrayList<MoveOutData>();
        long dleft = 0;
        long dright = 0;
        int lastMove = MOVE_NONE;
        for (WheelPosition p : positions) {
            long left = p.getLeft();
            long right = p.getRight();
            if (Long.signum(left) * Long.signum(right) < 0) {
                // rotation
                if (lastMove != MOVE_NONE && lastMove != MOVE_ROTATION) {
                    writeMove(moveData, lastMove, (int) dleft, (int) dright);
                    dleft = 0;
                    dright = 0;
                }
                lastMove = MOVE_ROTATION;
            } else {
                // ligne droite
                if (lastMove != MOVE_NONE && lastMove != MOVE_STRAIGHT) {
                    writeMove(moveData, lastMove, (int) dleft, (int) dright);
                    dleft = 0;
                    dright = 0;
                }
                lastMove = MOVE_STRAIGHT;
            }
            dleft += left;
            dright += right;
        }
        writeMove(moveData, lastMove, (int) dleft, (int) dright);
        return moveData;
    }

    @Override
    public void clear() {
        records.clear();
    }

    private void clearBuffer() {
        positions.clear();
        current = null;
    }

    protected List<MoveOutData> getMoveData() {
        return builMoveData();
    }

    @Override
    public List<TrajectoryRecord> getRecords() {
        return records;
    }

    public List<Point2D> getTrajectory() {
        List<Point2D> results = new ArrayList<Point2D>();
        double e, w;
        if (debug) {
            e = 0.2;
            w = (.075 * Math.PI) / 2000d;
        } else {
            IRobotDimension dimension = RobotUtils.getRobotAttribute(IRobotDimension.class, servicesProvider);
            IMotorProperties leftMotor = dimension.getLeftMotor();
            // TODO rightMotor
            w = leftMotor.getWheelPerimeter() / leftMotor.getPulseCount();
            e = dimension.getWheelDistance();
        }

        double alpha = 0;
        double x = 0, y = 0;
        double min, max;

        for (WheelPosition p : positions) {
            double left = w * p.getLeft();
            double right = w * p.getRight();

            if (left > right) {
                min = right;
                max = left;
            } else {
                min = left;
                max = right;
            }
            if (min == max) {
                max += 1e-9;
            }
            // rayon de courbure
            double r = e / ((max / min) - 1);
            // angle
            double theta = (min / r) % (Math.PI * 2);

            // distance parcourue par le point central
            double d = 2 * (r + e / 2) * Math.sin(theta / 2);
            // angle de la trajectoire
            double a = (Math.PI - theta) / 2;
            // variation sur l'axe x
            double dx = d * Math.sin(a);
            // variation sur l'axe y
            double dy = d * Math.cos(a);
            x += dx * Math.cos(alpha);
            y += dy * Math.sin(alpha);
            alpha += theta;
            Point2D position = new Point2D.Double(x, y);
            results.add(position);
        }
        return results;
    }

    protected List<WheelPosition> getWheelPositions() {
        return positions;
    }

    @PostConstruct
    public void initialize() {
    }

    @Override
    public boolean isRecording() {
        return (current != null);
    }

    @Override
    public void onInData(InData data) {
        if (data instanceof ReadPositionPulseInData) {
            ReadPositionPulseInData positionData = (ReadPositionPulseInData) data;
            positions.add(new WheelPosition(positionData.getLeft(), positionData.getRight()));
            synchronized (this) {
                notify();
            }
        }
    }

    private void pause() throws InterruptedException {
        Thread.sleep(PAUSE);
    }

    private void record() throws InterruptedException {
        comService.writeOutData(new StopOutData());
        comService.writeOutData(new ReadPositionPulseOutData());
        synchronized (this) {
            wait(TIMEOUT);
        }
    }

    @Override
    public void run() {
        LOGGER.info("trajectory recorder started");
        while (running) {
            try {
                if (paused) {
                    synchronized (this) {
                        wait();
                    }
                } else {
                    record();
                    pause();
                }
            } catch (InterruptedException e) {
                LOGGER.info("interrupted " + e.getMessage());
            }
        }
        LOGGER.info("trajectory recorder stopped");
    }

    public void setDebug(boolean debug) {
        if (debug) {
            positions.add(new WheelPosition(100, 100));
            positions.add(new WheelPosition(100, 100));
            positions.add(new WheelPosition(-1000, 1000));
            positions.add(new WheelPosition(-100, -100));
            positions.add(new WheelPosition(-200, -200));
            this.debug = true;
        }
    }

    @Override
    public void setServicesProvider(IRobotServiceProvider provider) {
        servicesProvider = provider;
        servicesProvider.registerService(ITrajectoryRecorder.class, this);
    }

    @PreDestroy
    public void shutdown() {
        LOGGER.info("shuting down trajectory");
        running = false;
        if (thread != null) {
            thread.interrupt();
        }
    }

    @Override
    public void start() {
        if (!paused) {
            return;
        }
        LOGGER.fine("starting recorder");
        clearBuffer();
        current = new TrajectoryRecord();
        comService.addInDataListener(this);
        paused = false;
        synchronized (this) {
            notify();
        }
    }

    @Override
    public void stop() {
        LOGGER.fine("stopping recorder");
        paused = true;
        current.setEndDate(new Date());
        current.setTrajectory(getTrajectory());
        records.add(current);
        clearBuffer();
    }

    private void writeMove(List<MoveOutData> moveData, int type, int left, int right) {
        switch (type) {
            case MOVE_ROTATION:
                moveData.add(new MoveOutData(left, -left, 0, 0));
            break;
            case MOVE_STRAIGHT:
                moveData.add(new MoveOutData(left, right, 0, 0));
            break;
        }
    }
}
