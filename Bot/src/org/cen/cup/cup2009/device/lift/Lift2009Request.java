package org.cen.cup.cup2009.device.lift;

import org.cen.cup.cup2009.device.Specific2009Device;
import org.cen.cup.cup2009.device.Specific2009Request;

/**
 * Request corresponding to the lift for 2009 edition.
 */
public class Lift2009Request extends Specific2009Request {
	public enum Action {
		/** Lift of 30 mm. */
		UP,
		/** Down of 30 mm. */
		DOWN,
		/** Small Up of 3 mm. */
		SMALL_UP,
		/** Small Down of 3 mm. */
		SMALL_DOWN,
		/** Go to the bottom. */
		BOTTOM;
	}

	private Action action;

	private int data;

	public Lift2009Request(Action action) {
		this(action, 0);
	}

	public Lift2009Request(Action action, int data) {
		super(Specific2009Device.NAME);
		this.action = action;
		this.data = data;
	}

	public Action getAction() {
		return action;
	}

	public int getData() {
		return data;
	}
}
