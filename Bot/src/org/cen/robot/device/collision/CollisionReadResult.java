package org.cen.robot.device.collision;

import java.awt.geom.Point2D;

import org.cen.robot.device.RobotDeviceRequest;

@Deprecated
public class CollisionReadResult extends CollisionResult {
	private Point2D obstaclePosition;

	public CollisionReadResult(RobotDeviceRequest request, Point2D obstaclePosition) {
		super(request);
		this.obstaclePosition = obstaclePosition;
	}

	public Point2D getObstaclePosition() {
		return obstaclePosition;
	}
}
