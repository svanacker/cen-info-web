package org.cen.robot.control;

/**
 * Distinguish theta / alpha control.
 */
public enum PIDInstructionType {

	/** For distance. */
	THETA(0, "theta"),

	/** For angle. */
	ALPHA(1, "alpha");

	public static final int COUNT = PIDInstructionType.values().length;

	public String getName() {
		return name;
	}

	/** Index. */
	private int index;

	private String name;

	private PIDInstructionType(int index, String name) {
		this.index = index;
		this.name = name;
	}

	public int getIndex() {
		return index;
	}
}
