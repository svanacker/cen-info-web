package org.cen.cup.cup2009.device.plier;

import org.cen.cup.cup2009.device.Specific2009Device;
import org.cen.cup.cup2009.device.Specific2009Request;

/**
 * The Request for the pliers for the 2009 cup edition.
 */
public class Plier2009Request extends Specific2009Request {
	public enum Action {
		/** Open the plier. */
		OPEN,
		/** Close the plier. */
		CLOSE,
		/** Move the plier to the left. */
		LEFT,
		/** Move the plier to the middle. */
		MIDDLE,
		/** Move the plier to the right. */
		RIGHT;
	}

	private Action action;

	public Plier2009Request(Action action) {
		super(Specific2009Device.NAME);
		this.action = action;
	}

	public Action getAction() {
		return action;
	}
}
