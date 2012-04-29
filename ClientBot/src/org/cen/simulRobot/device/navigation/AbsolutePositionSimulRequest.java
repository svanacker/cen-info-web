package org.cen.simulRobot.device.navigation;

import org.cen.robot.device.navigation.position.com.PositionStatus;

public class AbsolutePositionSimulRequest extends NavigationSimulRequest {
	double alpha;

	PositionStatus status;

	double x;

	double y;

	public AbsolutePositionSimulRequest(double x, double y, double alpha, PositionStatus status) {
		super(NavigationSimulDevice.NAME);
		this.x = x;
		this.y = y;
		this.alpha = alpha;
		this.status = status;
	}

	public double getAlpha() {
		return alpha;
	}

	public PositionStatus getStatus() {
		return status;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
}