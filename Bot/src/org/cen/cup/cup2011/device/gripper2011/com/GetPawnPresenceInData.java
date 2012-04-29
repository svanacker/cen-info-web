package org.cen.cup.cup2011.device.gripper2011.com;

import org.cen.com.in.InData;

public class GetPawnPresenceInData extends InData {

	public static final String HEADER = null;

	private final boolean pawnPresent;

	public GetPawnPresenceInData(boolean pawnPresent) {
		super();
		this.pawnPresent = pawnPresent;
	}

	public boolean isPawnPresent() {
		return pawnPresent;
	}
}
