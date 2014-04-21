package org.cen.cup.cup2011.device.vision2011;

import java.awt.geom.Point2D;

import org.cen.robot.device.RobotDeviceRequest;
import org.cen.robot.device.RobotDeviceResult;

public class PawnPositionResult extends RobotDeviceResult {
	private Point2D point;

	public PawnPositionResult(RobotDeviceRequest request, Point2D point) {
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
