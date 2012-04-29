package org.cen.robot.control;

/**
 * Encapsulates a structure which stores current and old Values of position, and
 * voltage supplied to the motor.
 */
public class MotionData {

	/** current position values. */
	protected float position;

	/** Old Position values. */
	protected float oldPosition;

	/** U (voltage) values. */
	protected float u;

	public MotionData() {

	}

	public float getPosition() {
		return position;
	}

	public void setPosition(float position) {
		this.position = position;
	}

	public float getOldPosition() {
		return oldPosition;
	}

	public void setOldPosition(float oldPosition) {
		this.oldPosition = oldPosition;
	}

	public float getU() {
		return u;
	}

	public void setU(float u) {
		this.u = u;
	}

}
