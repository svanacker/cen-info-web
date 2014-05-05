package org.cen.robot.device.navigation;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.cen.com.IComService;
import org.cen.com.InDataDecodingService;
import org.cen.com.in.InData;
import org.cen.com.in.InDataListener;
import org.cen.com.out.OutData;
import org.cen.logging.LoggingUtils;
import org.cen.robot.IRobotFactory;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.RobotDimension;
import org.cen.robot.RobotPosition;
import org.cen.robot.RobotUtils;
import org.cen.robot.device.AbstractRobotDevice;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.RobotDeviceRequest;
import org.cen.robot.device.navigation.NavigationResult.NavigationResultStatus;
import org.cen.robot.device.navigation.analysis.MotionAnalysisDecoder;
import org.cen.robot.device.navigation.com.BezierMoveOutData;
import org.cen.robot.device.navigation.com.EnableCollisionOutData;
import org.cen.robot.device.navigation.com.MoveBackwardOutData;
import org.cen.robot.device.navigation.com.MoveForwardOutData;
import org.cen.robot.device.navigation.com.NavigationDataDecoder;
import org.cen.robot.device.navigation.com.RotateLeftOneWheelOutData;
import org.cen.robot.device.navigation.com.RotateLeftOutData;
import org.cen.robot.device.navigation.com.RotateRightOneWheelOutData;
import org.cen.robot.device.navigation.com.RotateRightOutData;
import org.cen.robot.device.navigation.com.StopOutData;
import org.cen.robot.device.navigation.position.com.PositionInData;
import org.cen.robot.device.navigation.position.com.PositionStatus;
import org.cen.robot.device.navigation.position.com.ReadPositionPulseInData;
import org.cen.robot.device.navigation.position.com.ReadPositionPulseOutData;
import org.cen.robot.device.navigation.position.com.SetInitialPositionOutData;
import org.cen.util.StateMachineUtils;

public class NavigationDevice extends AbstractRobotDevice implements INavigationDevice, InDataListener {

    private boolean asynchronous;

    protected IComService comService;

    private NavigationDeviceContext fsm;

    private NavigationRequest request;

    private NavigationResult result;

    private static final Logger LOGGER = LoggingUtils.getClassLogger();

    public NavigationDevice() {
        super(NAME);
    }

    @Override
    public void debug(String debugAction) {
        if (debugAction.equals("reached")) {
            RobotPosition position = RobotUtils.getRobotAttribute(RobotPosition.class, servicesProvider);
            if (request instanceof MoveRequest) {
                MoveRequest r = (MoveRequest) request;
                position.go(r.getDistance());
            } else if (request instanceof RotationRequest) {
                RotationRequest r = (RotationRequest) request;
                position.rotateAngle(r.getAngle());
            } else if (request instanceof BezierMoveRequest) {
                BezierMoveRequest r = (BezierMoveRequest) request;
                Point2D p = r.getDestination();
                double x = p.getX();
                double y = p.getY();
                double angle = r.getAngle();
                position.set(x, y, angle);
            }
            // fsm.PositionRead(new PositionData(new ReadPositionInData(0, 0)));
            fsm.PositionReached();
            // fsm.PositionRead(new PositionData(new ReadPositionInData(0, 0)));
        } else if (debugAction.equals("positionRead")) {
            // fsm.PositionRead(new PositionData(new ReadPositionInData(0, 0)));
        }
    }

    public NavigationDeviceContext getFsm() {
        return fsm;
    }

    public List<OutData> getOutData(NavigationRequest navigationRequest) {
        List<OutData> list = new ArrayList<OutData>();

        // RobotDimension dimension =
        // RobotUtils.getRobotAttribute(RobotDimension.class, servicesProvider);

        // MotorProperties leftMotor = dimension.getLeftMotor();
        // MotorProperties rightMotor = dimension.getRightMotor();

        if (navigationRequest instanceof BezierMoveRequest) {
            BezierMoveRequest moveRequest = (BezierMoveRequest) navigationRequest;
            Point2D p = moveRequest.getDestination();
            double d1 = moveRequest.getD1();
            double d2 = moveRequest.getD2();
            double angle = moveRequest.getAngle();
            OutData outData = new BezierMoveOutData(p.getX(), p.getY(), angle, d1, d2);
            list.add(outData);
        } else if (navigationRequest instanceof MoveRequest) {
            MoveRequest moveRequest = (MoveRequest) navigationRequest;
            double distance = moveRequest.getDistance();
            // int leftDistance = (int) leftMotor.distanceToPulse(distance);
            // int rightDistance = (int) rightMotor.distanceToPulse(distance);
            // int acceleration = (int) moveRequest.getAcceleration();
            // int speed = (int) moveRequest.getSpeed();
            // list.add(new MoveOutData(leftDistance, rightDistance, speed,
            // acceleration));
            OutData outData = null;
            if (distance > 0) {
                outData = new MoveForwardOutData(distance);
            } else {
                outData = new MoveBackwardOutData(-distance);
            }
            list.add(outData);

        } else if (navigationRequest instanceof RotationRequest) {
            RotationRequest rotationRequest = (RotationRequest) navigationRequest;
            double angle = rotationRequest.getAngle();
            OutData outData = null;
            if (angle > 0) {
                outData = new RotateLeftOutData(angle);
            } else {
                outData = new RotateRightOutData(-angle);
            }
            list.add(outData);
            // double distance = (dimension.getWheelDistance() *
            // rotationRequest.getAngle()) / 2;
            // int leftDistance = (int) leftMotor.distanceToPulse(distance);
            // int rightDistance = (int) rightMotor.distanceToPulse(distance);
            // int acceleration = (int) rotationRequest.getAcceleration();
            // int speed = rotationRequest.getSpeed();
            // list.add(new MoveOutData(-leftDistance, rightDistance, speed,
            // acceleration));
        } else if (navigationRequest instanceof RotationOneWheelRequest) {
            RotationOneWheelRequest rotationOneWheelRequest = (RotationOneWheelRequest) navigationRequest;
            double angle = rotationOneWheelRequest.getAngle();
            OutData outData = null;
            if (angle > 0) {
                outData = new RotateLeftOneWheelOutData(angle);
            } else {
                outData = new RotateRightOneWheelOutData(-angle);
            }
            list.add(outData);
        } else if (navigationRequest instanceof StopRequest) {
            list.add(new StopOutData());
        } else if (navigationRequest instanceof SetInitialPositionRequest) {
            SetInitialPositionRequest r = (SetInitialPositionRequest) navigationRequest;
            SetInitialPositionOutData outData = new SetInitialPositionOutData(r.getX(), r.getY(), r.getOrientation());
            list.add(outData);
        } else if (navigationRequest instanceof EnableCollisionDetectionRequest) {
            EnableCollisionDetectionRequest r = (EnableCollisionDetectionRequest) request;
            list.add(new EnableCollisionOutData(r.isEnabled()));
        }
        return list;
    }

    private NavigationResult getResult(NavigationRequest request, NavigationResult.NavigationResultStatus status) {
        return new NavigationResult(request, status);
    }

    @Override
    public void initialize(IRobotServiceProvider servicesProvider) {
        super.initialize(servicesProvider);
        fsm = new NavigationDeviceContext(this);
        fsm.setName(NAME);
        comService = servicesProvider.getService(IComService.class);
        InDataDecodingService decodingService = comService.getDecodingService();

        NavigationDataDecoder navigationDecoder = new NavigationDataDecoder();
        decodingService.registerDecoder(navigationDecoder);

        MotionAnalysisDecoder analysisDecoder = new MotionAnalysisDecoder();
        decodingService.registerDecoder(analysisDecoder);

        comService.addInDataListener(this);
        IRobotFactory factory = servicesProvider.getService(IRobotFactory.class);
        StateMachineUtils.initialize(fsm, factory.getRobotConfiguration());
    }

    @Override
    protected void internalHandleRequest(RobotDeviceRequest request) {
        if (request instanceof SetInitialPositionRequest || request instanceof EnableCollisionDetectionRequest) {
            this.request = (NavigationRequest) request;
            sendData();
        } else if (request instanceof NavigationInitializeRequest) {
            fsm.Restart();
        } else if (request instanceof StopRequest) {
            fsm.Stop();
        } else if (request instanceof CollisionDetectionRequest) {
            fsm.CollisionDetected();
        } else if (request instanceof WaitPositionUpdateRequest) {
            fsm.ReadPosition();
        } else if (request instanceof NavigationRequest) {
            fsm.NewPosition((NavigationRequest) request);
        }
    }

    private boolean isAsynchronous() {
        return asynchronous;
    }

    private void notifyListeners(NavigationResult navigationResult) {
        IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
        handler.sendResult(this, navigationResult);
    }

    public void notifyRestart() {
        LOGGER.config("navigation device restarted");
    }

    void notifyResult() {
        notifyListeners(result);
    }

    @Override
    public void onInData(InData data) {
        if (data instanceof PositionInData) {
            PositionInData positionInData = (PositionInData) data;
            updateAbsolutePosition(positionInData.getX(), positionInData.getY(), positionInData.getAlpha());
            PositionStatus positionStatus = positionInData.getStatus();
            switch (positionStatus) {
            case OK:
            case FAILED:
                fsm.PositionReached();
                break;
            case BLOCKED:
                fsm.PositionFailed();
                break;
            }
        } else if (data instanceof ReadPositionPulseInData) {
            ReadPositionPulseInData d = (ReadPositionPulseInData) data;
            updatePosition(d.getLeft(), d.getRight());
        }
    }

    void readPosition() {
        System.out.println("reading position");
        send(new ReadPositionPulseOutData());
    }

    private void send(OutData data) {
        comService.writeOutData(data);
    }

    void sendData() {
        System.out.println("sending data " + request);
        List<OutData> outDataList = getOutData(request);
        for (OutData outData : outDataList) {
            send(outData);
        }
        notifyListeners(getResult(request, NavigationResultStatus.MOVING));
    }

    public void setAsynchronous(boolean asynchronous) {
        this.asynchronous = asynchronous;
    }

    public void setCollision() {
        result = getResult(request, NavigationResultStatus.COLLISION);
        fsm.Next();
    }

    void setInterrupted() {
        result = getResult(request, NavigationResultStatus.INTERRUPTED);
        fsm.Next();
    }

    void setReached() {
        result = getResult(request, NavigationResultStatus.REACHED);
        fsm.Next();
    }

    void setFailed() {
        result = getResult(request, NavigationResultStatus.STOPPED);
        fsm.Next();
    }

    void setRequest(NavigationRequest request) {
        this.request = request;
    }

    void setStopped() {
        send(new StopOutData());
        result = getResult(request, NavigationResultStatus.STOPPED);
        fsm.Next();
    }

    private void sleep(int timeInMillis) {
        try {
            Thread.sleep(timeInMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void unhandled() {
        System.out.println("unhandled transition: " + fsm.getTransition());
    }

    private void updateAbsolutePosition(double x, double y, double alpha) {
        LOGGER.fine("updating robot position");
        RobotPosition position = RobotUtils.getRobotAttribute(RobotPosition.class, servicesProvider);
        position.set(x, y, alpha);
    }

    private void updatePosition(long left, long right) {
        LOGGER.fine("updating robot position");
        RobotPosition position = RobotUtils.getRobotAttribute(RobotPosition.class, servicesProvider);
        RobotDimension dimension = RobotUtils.getRobotAttribute(RobotDimension.class, servicesProvider);
        position.updateFromPulses(left, right, dimension);
    }
}