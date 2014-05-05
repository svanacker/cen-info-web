package org.cen.cup.cup2009.navigation;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import org.cen.cup.cup2009.device.Sequence2009Request;
import org.cen.logging.LoggingUtils;
import org.cen.navigation.INavigationMap;
import org.cen.navigation.ITrajectoryService;
import org.cen.navigation.Location;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.RobotPosition;
import org.cen.robot.RobotUtils;
import org.cen.robot.device.DeviceRequestDispatcher;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.RobotDeviceListener;
import org.cen.robot.device.RobotDeviceRequest;
import org.cen.robot.device.RobotDeviceResult;
import org.cen.robot.device.navigation.INavigationDevice;
import org.cen.robot.device.navigation.MoveRequest;
import org.cen.robot.device.navigation.NavigationDevice;
import org.cen.robot.device.navigation.NavigationRequest;
import org.cen.robot.device.navigation.NavigationResult;
import org.cen.robot.device.navigation.StopRequest;
import org.cen.robot.match.MatchData;
import org.cen.util.Holder;

public class NavigationHandler2009 implements RobotDeviceListener {
    /** Instruction to disable the detection of collision. */
    private static final String COLLISION_OFF = "c_off";

    /** Instruction to enable the detection of collision. */
    private static final String COLLISION_ON = "c_on";

    /** Number of trajectory phases. */
    private static final int DEFAULT_PHASES_COUNT = 3;

    private static final int DISTANCE_FROM_CENTER = 175;

    // General

    /** Instruction so that the robot go back. */
    private static final String ROBOT_BACK = "^b";

    /** Instruction to take a column. */
    private static final String TAKE_COLUMN = "l";

    /** Instruction to wait a number of seconds. */
    private static final String WAIT_TIME = "t";

    // Step 1

    /** Instruction to prepare to build the columns. */
    private static final String PREPARE_TO_BUILD_STEP_1 = "p";

    /**
     * Instruction to build the columns in the first phasis in construction type
     * 1.
     */
    private static final String FIRST_BUILD_COLUMNS_TYPE_1 = "u1";

    /**
     * Instruction to build the lintel in the first phasis in construction type
     * 1.
     */
    private static final String FIRST_BUILD_LINTEL_TYPE_1 = "ulin1";

    /**
     * Instruction to build the columns in the first phasis in construction type
     * 2.
     */
    private static final String FIRST_BUILD_COLUMNS_AND_LINTEL_TYPE_2 = "u2";

    /** Instruction raised at the init. */
    private static final String INIT = "i";

    // Phase 2 / Type 1

    /** Prepare to build columns. */
    private static final String PREPARE_TO_BUILD_SECOND_CONSTRUCTION = "q";

    /** Build 2 Columns on (2x2) + 1 lintel). */
    private static final String BUILD_2_COLUMNS_ON_TOP = "v1";

    // Phase 2 / Type 2

    /** Instruction to prepare to load the second lintel. */
    private static final String PREPARE_TO_LOAD_SECOND_LINTEL = "p2";

    /** Instruction to load the second lintel. */
    private static final String LOAD_SECOND_LINTEL = "llin2";

    private static final Logger LOGGER = LoggingUtils.getClassLogger();

    private static final String PROPERTY_ASYNCHRONOUS = "asynchronous";

    private static final String PROPERTY_ASYNCHRONOUS_DEFAULT_VALUE = "true";

    private static final String PROPERTY_TRAJECTORY_PHASES = "trajectoryPhases";

    /** Instruction to build in the second phasis. */
    private static final String SECOND_BUILD_LINTEL_STEP_2 = "v2";

    private boolean asynchronous = true;

    private int nextRequest;

    /** Number of actual trajectory phases. */
    private int phases = DEFAULT_PHASES_COUNT;

    private Properties properties;

    private final BlockingQueue<RobotDeviceRequest> queue = new ArrayBlockingQueue<RobotDeviceRequest>(5, true);

    private List<RobotDeviceRequest> requests;

    private Thread sender;

    private IRobotServiceProvider servicesProvider;

    protected boolean terminated = false;

    public NavigationHandler2009(IRobotServiceProvider servicesProvider) {
        super();
        this.servicesProvider = servicesProvider;

        IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
        handler.addDeviceListener(this);
    }

    @Override
    public String getDeviceName() {
        return INavigationDevice.NAME;
    }

    public int getPhasesCount() {
        return phases;
    }

    public List<RobotDeviceRequest> getRequests(int configurationId, int trajectoryIndex, String suffix,
            Holder<Double> initialOrientation) {
        MatchData data = RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);
        String prefix = "";
        switch (data.getSide()) {
        case RED:
            prefix = "green-";
            break;
        case VIOLET:
            prefix = "red-";
            break;
        }
        List<RobotDeviceRequest> requests = new ArrayList<RobotDeviceRequest>();
        List<Point2D> path = new ArrayList<Point2D>();

        // initial orientation of the robot
        double orientation = initialOrientation.getValue();
        if (Double.isNaN(orientation)) {
            orientation = RobotUtils.getRobotAttribute(RobotPosition.class, servicesProvider).getAlpha();
        }

        ITrajectoryService trajectoryService = servicesProvider.getService(ITrajectoryService.class);
        INavigationMap map = trajectoryService.getNavigationMap();
        Map<String, Location> locations = new HashMap<String, Location>();
        for (Location l : map.getLocations()) {
            locations.put(l.getName(), l);
        }
        // trajectory key
        String key = "trajectory" + configurationId + "." + trajectoryIndex;
        if ((suffix != null) && !suffix.isEmpty()) {
            key += "." + suffix;
        }
        // tarjectory data
        String s = properties.getProperty(key);
        if (s != null) {
            boolean central = false;
            boolean backward = false;
            String[] positionIds = s.split(",");
            for (String id : positionIds) {
                // Commands
                if (id.startsWith("^")) {
                    if (id.equals(ROBOT_BACK)) {
                        backward ^= true;
                    } else {
                        handleCommand(requests, id.substring(1));
                    }
                } else {
                    Location l = locations.get(prefix + id);
                    if (l == null) {
                        LOGGER.severe("Location with " + (prefix + id) + " does not exist !");
                    } else {
                        central |= id.equals("c");
                        if (l != null) {
                            path.add(l.getPosition());
                            if (path.size() > 1) {
                                orientation = trajectoryService.buildTrajectoryRequests(path, orientation, requests,
                                        backward);
                                // r�duit la trajectoire vers ou depuis le point
                                // central
                                if (central) {
                                    handleCentralPosition(requests);
                                    central = l.getName().equals(prefix + "c");
                                }
                                path.remove(0);
                            }
                        }
                    }
                }
            }
        }
        initialOrientation.setValue(orientation);
        return requests;
    }

    private void handleCentralPosition(List<RobotDeviceRequest> requests) {
        RobotDeviceRequest r = requests.get(requests.size() - 1);
        if (r instanceof MoveRequest) {
            MoveRequest mr = (MoveRequest) r;
            double d = mr.getDistance();
            if (d > 0) {
                d -= DISTANCE_FROM_CENTER;
            } else {
                d += DISTANCE_FROM_CENTER;
            }
            r = new MoveRequest(d);
            requests.remove(requests.size() - 1);
            requests.add(r);
        }
    }

    private void handleCommand(List<RobotDeviceRequest> requests, String command) {
        if (command.equals(TAKE_COLUMN)) {
            RobotDeviceRequest request = new Sequence2009Request(Sequence2009Request.Action.TAKE_COLUMN);
            requests.add(request);
            // Phase 1
        } else if (command.equals(PREPARE_TO_BUILD_STEP_1)) {
            RobotDeviceRequest request = new Sequence2009Request(
                    Sequence2009Request.Action.PREPARE_TO_BUILD_FIRST_CONSTRUCTION);
            requests.add(request);
        } else if (command.equals(FIRST_BUILD_COLUMNS_TYPE_1)) {
            RobotDeviceRequest request = new Sequence2009Request(Sequence2009Request.Action.FIRST_BUILD_COLUMNS_TYPE_1);
            requests.add(request);
            // Type 1
        } else if (command.equals(FIRST_BUILD_LINTEL_TYPE_1)) {
            RobotDeviceRequest request = new Sequence2009Request(Sequence2009Request.Action.FIRST_BUILD_LINTEL_TYPE1);
            requests.add(request);
            // Type 2
        } else if (command.equals(FIRST_BUILD_COLUMNS_AND_LINTEL_TYPE_2)) {
            RobotDeviceRequest request = new Sequence2009Request(
                    Sequence2009Request.Action.FIRST_BUILD_COLUMNS_AND_LINTEL_TYPE_2);
            requests.add(request);
            // Phase 2 / Type 1
        } else if (command.equals(PREPARE_TO_BUILD_SECOND_CONSTRUCTION)) {
            RobotDeviceRequest request = new Sequence2009Request(
                    Sequence2009Request.Action.PREPARE_TO_BUILD_SECOND_CONSTRUCTION);
            requests.add(request);
        } else if (command.equals(BUILD_2_COLUMNS_ON_TOP)) {
            RobotDeviceRequest request = new Sequence2009Request(Sequence2009Request.Action.BUILD_2_COLUMNS_ON_TOP);
            requests.add(request);

            // Phase 2 / Type 2
        } else if (command.equals(PREPARE_TO_LOAD_SECOND_LINTEL)) {
            RobotDeviceRequest request = new Sequence2009Request(
                    Sequence2009Request.Action.PREPARE_TO_LOAD_SECOND_LINTEL);
            requests.add(request);
        } else if (command.equals(LOAD_SECOND_LINTEL)) {
            RobotDeviceRequest request = new Sequence2009Request(Sequence2009Request.Action.LOAD_SECOND_LINTEL);
            requests.add(request);
        } else if (command.equals(SECOND_BUILD_LINTEL_STEP_2)) {
            RobotDeviceRequest request = new Sequence2009Request(Sequence2009Request.Action.SECOND_LINTEL_CONSTRUCTION);
            requests.add(request);
        }
        // General
        else if (command.startsWith(INIT)) {
            RobotDeviceRequest request = new Sequence2009Request(Sequence2009Request.Action.INIT);
            requests.add(request);
        } else if (command.startsWith(COLLISION_OFF)) {
            RobotDeviceRequest request = new Sequence2009Request(Sequence2009Request.Action.DISABLE_COLLISION_DETECTION);
            requests.add(request);
        } else if (command.startsWith(COLLISION_ON)) {
            RobotDeviceRequest request = new Sequence2009Request(Sequence2009Request.Action.ENABLE_COLLISION_DETECTION);
            requests.add(request);
        } else if (command.startsWith(WAIT_TIME)) {
            String timeAsString = command.substring(1);
            int time = Integer.valueOf(timeAsString).intValue();
            RobotDeviceRequest request = new Sequence2009Request(Sequence2009Request.Action.TIME, time);
            requests.add(request);
        }
    }

    @Override
    public void handleResult(RobotDeviceResult result) {
        if (result instanceof NavigationResult) {
            NavigationResult r = (NavigationResult) result;
            switch (r.getStatus()) {
            case COLLISION:
                break;
            case EMPTYQUEUE:
                break;
            case ENQUEUED:
                break;
            case INTERRUPTED:
                break;
            case MOVING:
                break;
            case REACHED:
                sendNextRequest();
                break;
            case STOPPED:
                break;
            }
        }
    }

    public boolean isAsynchronous() {
        return asynchronous;
    }

    private void sendNextRequest() {
        if (nextRequest <= requests.size()) {
            RobotDeviceRequest r = requests.get(nextRequest++);
            queue.offer(r);
        }
    }

    public void setAsynchronous(boolean asynchronous) {
        this.asynchronous = asynchronous;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
        String s = properties.getProperty(PROPERTY_TRAJECTORY_PHASES);
        if (s != null) {
            try {
                phases = Integer.parseInt(s);
            } catch (Exception e) {
                LOGGER.warning(e.getMessage());
                e.printStackTrace();
            }
        }
        LOGGER.fine("trajectory phases: " + phases);

        // Asynchronous trajectory
        s = properties.getProperty(PROPERTY_ASYNCHRONOUS, PROPERTY_ASYNCHRONOUS_DEFAULT_VALUE);
        asynchronous = Boolean.valueOf(s);
        LOGGER.fine("asynchronous: " + asynchronous);
    }

    public void setServicesProvider(IRobotServiceProvider provider) {
        this.servicesProvider = provider;
    }

    public void shutdown() {
        terminated = true;
        if (sender != null) {
            sender.interrupt();
        }
        sender = null;
        IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
        handler.removeDeviceListener(this);
        handler.getRequestDispatcher().purgeQueue();
    }

    private void startAsynchronousTrajectory() {
        LOGGER.fine("starting asynchronous trajectory");

        MatchData data = RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);
        requests = (List<RobotDeviceRequest>) data.get("requests");
        nextRequest = 0;

        IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
        final DeviceRequestDispatcher dispatcher = handler.getRequestDispatcher();

        // Mode asynchrone
        NavigationDevice device = (NavigationDevice) handler.getDevice(INavigationDevice.NAME);
        device.setAsynchronous(true);

        sender = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!terminated) {
                    try {
                        RobotDeviceRequest r = queue.take();
                        dispatcher.sendRequest(r);
                        if (!(r instanceof NavigationRequest)) {
                            sendNextRequest();
                        }
                    } catch (InterruptedException e) {
                        LOGGER.finest("sending thread interrupted");
                        e.printStackTrace();
                    }
                }
            }
        }, "AsynchronousTrajectoryThread");
        sender.start();
        sendNextRequest();
    }

    private void startSynchronousTrajectory() {
        LOGGER.fine("starting synchronous trajectory");

        MatchData data = RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);
        final List<RobotDeviceRequest> requests = (List<RobotDeviceRequest>) data.get("requests");

        IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
        final DeviceRequestDispatcher dispatcher = handler.getRequestDispatcher();

        // Mode synchrone
        NavigationDevice device = (NavigationDevice) handler.getDevice(INavigationDevice.NAME);
        device.setAsynchronous(false);

        sender = new Thread(new Runnable() {
            @Override
            public void run() {
                for (RobotDeviceRequest r : requests) {
                    if (r instanceof NavigationRequest && !(r instanceof StopRequest)) {
                        dispatcher.sendRequest(new StopRequest());
                    }
                    dispatcher.sendRequest(r);

                    if (terminated) {
                        break;
                    }
                }
            }
        }, "SynchronousTrajectoryThread");
        sender.start();
    }

    public void startTrajectory() {
        if (asynchronous) {
            startAsynchronousTrajectory();
        } else {
            startSynchronousTrajectory();
        }
    }
}
