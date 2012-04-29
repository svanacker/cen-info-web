package org.cen.cup.cup2009.device.lintel;

import org.cen.cup.cup2009.device.Specific2009Device;
import org.cen.cup.cup2009.device.Specific2009Request;

public class Lintel2009Request extends Specific2009Request {
	public enum Action {
		/** Open the lintel plier. */
		OPEN,
		/** Close the lintel plier. */
		CLOSE,
		/** Deploy the lintel plier. */
		DEPLOY,
		/** UnDeploy the lintel plier. */
		UNDEPLOY;
	}

	private Action action;

	public Lintel2009Request(Action action) {
		super(Specific2009Device.NAME);
		this.action = action;
	}

	public Action getAction() {
		return action;
	}
}
