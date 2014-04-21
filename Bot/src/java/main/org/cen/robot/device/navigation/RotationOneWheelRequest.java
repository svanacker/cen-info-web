package org.cen.robot.device.navigation;

/**
 * Request which ask the robot to rotate with a certain angle, but with only one
 * Wheel.
 */
public final class RotationOneWheelRequest extends NavigationRequest {

	private final double angle;

	public RotationOneWheelRequest(double angle) {
		super();
		this.angle = angle;
	}

	public double getAngle() {
		return angle;
	}

	@Override
	public String toString() {
		return getClass().getName() + "{angle: " + angle + "}";
	}
}