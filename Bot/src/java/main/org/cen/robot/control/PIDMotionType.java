package org.cen.robot.control;

public enum PIDMotionType {

	/**
	 * The robot goes forward or backward.
	 */
	MOTION_TYPE_FORWARD_OR_BACKWARD(0x00),

	/**
	 * The robot do a rotation.
	 */
	MOTION_TYPE_ROTATION(0x01),

	/**
	 * The robot do a rotation with One Wheel
	 */
	MOTION_TYPE_ROTATION_ONE_WHEEL(0x02),

	/**
	 * The robot must maintain a position.
	 */
	MOTION_TYPE_MAINTAIN_POSITION(0x03);

	private int index;

	private PIDMotionType(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}
}
