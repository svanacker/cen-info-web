package org.cen.cup.cup2011.device.gripper2011;

import org.cen.robot.match.IMatchEvent;

public class PawnPresenceEvent implements IMatchEvent {
	private boolean pawnPresent;

	public PawnPresenceEvent(boolean pawnPresent) {
		super();
		this.pawnPresent = pawnPresent;
	}

	public boolean isPawnPresent() {
		return pawnPresent;
	}
}
