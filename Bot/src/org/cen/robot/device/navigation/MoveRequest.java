package org.cen.robot.device.navigation;

/**
 * Request to move the robot with slavery (trapezoidal movement, control of
 * acceleration, control of speed...).
 */
public final class MoveRequest extends NavigationRequest {
	private double acceleration;

	private double distance;

	private double speed;

	public MoveRequest(double distance) {
		super();
		// To avoid problems of triangle trajectory
		// if (Math.abs(distance) < 100) {
		// speed = 4;
		// } else if (Math.abs(distance) < 200) {
		// speed = 8;
		// } else if (Math.abs(distance) < 500) {
		// speed = 10;
		// } else {
		// speed = 16;
		// }
		// this.acceleration = speed;
		this.distance = distance;
		this.speed = 0x20;
		this.acceleration = this.speed / 2;
	}

	public MoveRequest(double distance, double speed, double acceleration) {
		super();
		this.distance = distance;
		this.speed = speed;
		this.acceleration = acceleration;
	}

	public int getEstimatedTime() {
		return 1000 + Math.abs((int) (distance / speed) * 32);
	}

	public double getAcceleration() {
		return acceleration;
	}

	public double getDistance() {
		return distance;
	}

	public double getSpeed() {
		return speed;
	}

	@Override
	public String toString() {
		return getClass().getName() + "[distance: " + distance + ", speed=" + speed + ", a=" + acceleration + ", estimatedTime=" + getEstimatedTime() + "]";
	}
}
