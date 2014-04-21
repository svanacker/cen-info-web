package org.cen.cup.cup2011.device.gripper2011.com;

import org.cen.com.in.InData;

public class GetKingPresenceInData extends InData {
	public static final String HEADER = ">";

	private long value;

	public GetKingPresenceInData(long value) {
		super();
		this.value = value;
	}

	public long getValue() {
		return value;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[value=" + value + "]";
	}
}
