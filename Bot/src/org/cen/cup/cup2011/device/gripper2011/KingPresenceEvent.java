package org.cen.cup.cup2011.device.gripper2011;

import org.cen.robot.match.IMatchEvent;

public class KingPresenceEvent implements IMatchEvent {
	private boolean pawnPresent;

	public KingPresenceEvent(boolean pawnPresent) {
		super();
		this.pawnPresent = pawnPresent;
	}

	public boolean isKingPresent() {
		return pawnPresent;
	}
}
