package org.cen.cup.cup2009.device;

/**
 * Request for useful Sequences for 2009 cup edition.
 */
public class Sequence2009Request extends Specific2009Request {
	public enum Action {
		/** Done at the init. */
		INIT,
		/** Wait a few seconds. */
		TIME,
		/** Enable the detection of the collision. */
		ENABLE_COLLISION_DETECTION,
		/** Disable the detection of the collision. */
		DISABLE_COLLISION_DETECTION,
		/** Take a column. */
		TAKE_COLUMN,
		/** Prepare to build the column on the colline. */
		PREPARE_TO_BUILD_FIRST_CONSTRUCTION,
		/** Go forward. */
		FORWARD,
		/** Build 4 Columns Construction (2 x 2). */
		FIRST_BUILD_COLUMNS_TYPE_1,
		/** Build 1 Lintel on the (2x2 columns elements. */
		FIRST_BUILD_LINTEL_TYPE1,
		/** Build 4 Columns Construction (2 x 1), lintel and (2 x 1) column element */
		FIRST_BUILD_COLUMNS_AND_LINTEL_TYPE_2,
		/** Prepare to load the lintel. */
		PREPARE_TO_LOAD_SECOND_LINTEL,
		/** Load the second Lintel */
		LOAD_SECOND_LINTEL,
		/** Let the second lintel on the construction. */
		SECOND_LINTEL_CONSTRUCTION,
		/** Prepare for the second column construction. */
		PREPARE_TO_BUILD_SECOND_CONSTRUCTION,
		/** Build 2 Columns on (2x2) + 1 lintel). */
		BUILD_2_COLUMNS_ON_TOP
	}

	private Action action;
	
	/** A value if the action required it. */
	private int value;

	public Sequence2009Request(Action action) {
		super(Specific2009Device.NAME);
		this.action = action;
	}
	
	public Sequence2009Request(Action action, int value) {
		super(Specific2009Device.NAME);
		this.action = action;
		this.value = value;
	}

	public Action getAction() {
		return action;
	}

	public int getValue() {
		return value;
	}
}
