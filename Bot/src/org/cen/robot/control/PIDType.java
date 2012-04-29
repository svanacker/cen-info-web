package org.cen.robot.control;

/**
 * Define the type of pid used for each type of move.
 */
public enum PIDType {

	/**
	 * The mask used to indicate that we want to use value of PID to go forward
	 * and backward.
	 */
	GO(0, "go"),

	/** The mask used to indicate that we want to use value of PID to rotate. */
	ROTATE(1, "rotate"),

	/**
	 * The mask used to indicate that we want to use value of PID to maintain
	 * very strongly the position.
	 */
	MAINTAIN_POSITION(2, "maintain"),

	/**
	 * The mask used to indicate that we want to adjust the robot to the border
	 * of board.
	 */
	ADJUST_DIRECTION(3, "adjust"),

	/** The pid for final approach. */
	FINAL_APPROACH(4, "finalApproach");

	public static final int COUNT = PIDType.values().length;

	private int index;

	private String name;

	private PIDType(int index, String name) {
		this.index = index;
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}
}
