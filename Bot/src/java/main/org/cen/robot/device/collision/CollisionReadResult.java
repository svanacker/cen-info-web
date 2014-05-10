package org.cen.robot.device.collision;

import org.cen.geom.Point2D;
import org.cen.robot.device.request.IRobotDeviceRequest;

@Deprecated
public class CollisionReadResult extends CollisionResult {
    private final Point2D obstaclePosition;

    public CollisionReadResult(IRobotDeviceRequest request, Point2D obstaclePosition) {
        super(request);
        this.obstaclePosition = obstaclePosition;
    }

    public Point2D getObstaclePosition() {
        return obstaclePosition;
    }
}
