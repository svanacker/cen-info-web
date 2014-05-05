package org.cen.robot.control;

/**
 * Type of motion.
 */
public enum MotionProfileType {

	TRIANGLE(0),

	TRAPEZE(1);

	private int index;

	public int getIndex() {
		return index;
	}

	private MotionProfileType(int index) {
		this.index = index;
	}
}
