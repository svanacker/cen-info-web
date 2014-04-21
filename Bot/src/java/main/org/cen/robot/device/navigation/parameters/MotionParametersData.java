package org.cen.robot.device.navigation.parameters;

/**
 * Stores the motion Parameters.
 */
public class MotionParametersData {

	private String name;

	private int motionType;

	private int acceleration;

	private int speed;

	public MotionParametersData(int motionType, int acceleration, int speed) {
		super();
		this.motionType = motionType;
		this.acceleration = acceleration;
		this.speed = speed;
	}

	public MotionParametersData() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMotionType() {
		return motionType;
	}

	public int getAcceleration() {
		return acceleration;
	}

	public int getSpeed() {
		return speed;
	}

	public void setMotionType(int motionType) {
		this.motionType = motionType;
	}

	public void setAcceleration(int acceleration) {
		this.acceleration = acceleration;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[type=" + motionType + ", a=" + acceleration + ", speed=" + speed + "]";
	}
}
