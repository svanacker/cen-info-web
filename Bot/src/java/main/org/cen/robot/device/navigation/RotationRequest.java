package org.cen.robot.device.navigation;

/**
 * Request which ask the robot to rotate with a certain angle.
 */
public final class RotationRequest extends NavigationRequest {

	private final double angle;

	private final int acceleration;

	private final int speed;

	public RotationRequest(double angle) {
		super();
		this.angle = angle;
		this.speed = 0xA;
		this.acceleration = this.speed / 2;
	}

	public RotationRequest(double angle, int speed, int acceleration) {
		super();
		this.acceleration = acceleration;
		this.angle = angle;
		this.speed = speed;
	}

	public double getAcceleration() {
		return acceleration;
	}

	public double getAngle() {
		return angle;
	}

	public int getSpeed() {
		return speed;
	}

	@Override
	public String toString() {
		return getClass().getName() + "[angle: " + angle + ", speed=" + speed + ", a=" + acceleration + "]";
	}
}
