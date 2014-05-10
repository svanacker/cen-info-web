package org.cen.cup.cup2011.device.vision2011;

import org.cen.geom.Point2D;
import org.cen.robot.device.RobotDeviceResult;
import org.cen.robot.device.request.IRobotDeviceRequest;

public class PawnPositionResult extends RobotDeviceResult {
    private final Point2D point;

    public PawnPositionResult(IRobotDeviceRequest request, Point2D point) {
        super(request);
        this.point = point;
    }

    public Point2D getPoint() {
        return point;
    }

    public boolean isTerminated() {
        return Double.isNaN(point.getX()) || Double.isNaN(point.getY());
    }
}
