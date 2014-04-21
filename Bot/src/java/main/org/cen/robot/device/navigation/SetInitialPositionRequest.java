package org.cen.robot.device.navigation;

public class SetInitialPositionRequest extends NavigationRequest {
	private final double x;

	private final double y;

	private final double orientation;

	public SetInitialPositionRequest(double x, double y, double orientation) {
		super();
		this.x = x;
		this.y = y;
		this.orientation = orientation;
	}

	public double getOrientation() {
		return orientation;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
}
