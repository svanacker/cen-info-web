package org.cen.simulRobot.brain.navigation;

import org.cen.navigation.TrajectoryCurve;
import org.cen.navigation.TrajectoryCurve.Direction;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.RobotDimension;
import org.cen.robot.RobotPosition;
import org.cen.robot.RobotUtils;
import org.cen.robot.brain.AbstractDeviceHandler;
import org.cen.robot.device.DeviceRequestDispatcher;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.RobotDeviceResult;
import org.cen.simulRobot.device.navigation.AbsolutePositionSimulRequest;
import org.cen.simulRobot.device.navigation.NavigationSimulDevice;
import org.cen.simulRobot.device.navigation.NavigationSimulReadResult;
import org.cen.simulRobot.device.navigation.StopSimulResult;
import org.cen.simulRobot.match.ISimulMatchStrategy;
import org.cen.simulRobot.match.event.MoveEvent;
import org.cen.simulRobot.match.event.StopEvent;
import org.cen.simulRobot.match.simulMoving.MovingHandlerListener;
import org.cen.simulRobot.match.simulMoving.event.SimulMovedEvent;
import org.cen.ui.gameboard.IGameBoardService;
import org.cen.ui.gameboard.ISimulGameBoard;

public class NavigationSimulHandler extends AbstractDeviceHandler implements MovingHandlerListener {

    public static final String NAME = NavigationSimulDevice.NAME;

    private DeviceRequestDispatcher dispatcher;

    private RobotDimension robotDimension;

    private double speed;

    /**
     * Constructor.
     * 
     * @param servicesProvider the services provider
     */
    public NavigationSimulHandler(IRobotServiceProvider aservicesProvider) {
        super(aservicesProvider);
        IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
        dispatcher = handler.getRequestDispatcher();
        robotDimension = RobotUtils.getRobotAttribute(RobotDimension.class, servicesProvider);
        speed = 0.25;
        ISimulGameBoard gameBoard = (ISimulGameBoard) aservicesProvider.getService(IGameBoardService.class);
        gameBoard.getMovingHandler().addDeviceListener(this);
    }

    // TODO methode de RobotPosition. Trouver une façon de ne pas la copier
    private void computeCurve(TrajectoryCurve curve, double width) {
        if (Math.signum(curve.getLeftWheel()) == Math.signum(curve.getRightWheel())) {
            // ICR is outside of the wheels
            computeLargeRadiusCurve(curve, width);
        } else {
            // ICR is between the wheels
            computeNarrowRadiusCurve(curve, width);
        }
    }

    // TODO methode de RobotPosition. Trouver une façon de ne pas la copier
    private void computeLargeRadiusCurve(TrajectoryCurve curve, double width) {
        // min and max curve distance values
        double min, max;
        Direction direction;
        double left = curve.getLeftWheel();
        double right = curve.getRightWheel();
        int c = Double.compare(Math.abs(left), Math.abs(right));
        switch (c) {
            case 0:
                // straight (abs(left) = abs(right))
                curve.setResults(0, Double.MAX_VALUE, left, Direction.LEFT);
                return;
            case 1:
                // turn right (abs(left) > abs(right))
                min = right;
                max = left;
                direction = Direction.RIGHT;
            break;
            default:
                // turn left (abs(left) < abs(right))
                min = left;
                max = right;
                direction = Direction.LEFT;
            break;
        }
        double theta = max - min;
        double angle = theta / width;
        double radius = Math.abs((width / 2.0f) * (max + min) / theta);
        double distance = angle * radius;
        curve.setResults(angle, radius, distance, direction);
    }

    // TODO methode de RobotPosition. Trouver une façon de ne pas la copier
    private void computeNarrowRadiusCurve(TrajectoryCurve curve, double width) {
        // min and max curve distance values
        double max, min;
        double left = curve.getLeftWheel();
        double right = curve.getRightWheel();
        Direction direction;
        int c = Double.compare(Math.abs(left), Math.abs(right));
        switch (c) {
            case 0:
                // straight (abs(left) = abs(right))
                if (Double.compare(left, right) == 0) {
                    curve.setResults(0, Double.MAX_VALUE, left, Direction.LEFT);
                    return;
                } else {
                    min = Math.min(left, right);
                    max = Math.max(left, right);
                    if (min == left) {
                        direction = Direction.LEFT;
                    } else {
                        direction = Direction.RIGHT;
                    }
                }
            case 1:
                // turn right (abs(left) > abs(right))
                max = left;
                min = right;
                direction = Direction.RIGHT;
            break;
            default:
                // turn left (abs(left) < abs(right))
                min = left;
                max = right;
                direction = Direction.LEFT;
            break;
        }
        double theta = Math.abs(min) + Math.abs(max);
        double angle = (theta / width) * Math.signum(max);
        double radius = (width / 2.0f) * (Math.abs(max) - Math.abs(min)) / theta;
        double distance = angle * radius;
        curve.setResults(angle, radius, distance, direction);
    }

    @Override
    public String getHandlerName() {
        return NAME;
    }

    @Override
    public String getDeviceName() {
        return NAME;
    }

    public double getSpeed() {
        return speed;
    }

    @Override
    public void handleResult(RobotDeviceResult result) {
        if (result instanceof NavigationSimulReadResult) {
            // transformation du deplacement en une position relative au robot
            int left = ((NavigationSimulReadResult) result).getLeft();
            int right = ((NavigationSimulReadResult) result).getRight();
            RobotPosition relativeRobotPosition = new RobotPosition(0, 0, 0);
            relativeRobotPosition.updateFromPulses(left, right, robotDimension);

            // get the distance and final orientation
            double l = robotDimension.getLeftMotor().pulseToDistance(left);
            double r = robotDimension.getLeftMotor().pulseToDistance(right);
            TrajectoryCurve curve = new TrajectoryCurve(l, r);
            computeCurve(curve, robotDimension.getWheelDistance());
            double distance = curve.getDistance();

            // Send a moveEvent wich throw a move on the GameBoard
            ISimulMatchStrategy simulStrategy = servicesProvider.getService(ISimulMatchStrategy.class);
            simulStrategy.notifyEvent(new MoveEvent(curve, speed, NAME));
        } else if (result instanceof StopSimulResult) {
            ISimulMatchStrategy simulStrategy = servicesProvider.getService(ISimulMatchStrategy.class);
            simulStrategy.notifyEvent(new StopEvent(NAME));
        }
    }

    @Override
    public void onMovingHandlerData(SimulMovedEvent event) {
        SimulMovedEvent tempEvent = event;
        dispatcher.sendRequest(new AbsolutePositionSimulRequest(tempEvent.getX(), tempEvent.getY(), tempEvent
                .getAlpha(), tempEvent.getStatus()));
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

}
