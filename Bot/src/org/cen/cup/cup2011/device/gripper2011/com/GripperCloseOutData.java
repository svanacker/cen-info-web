package org.cen.cup.cup2011.device.gripper2011.com;

import org.cen.com.out.OutData;

public class GripperCloseOutData extends OutData {
	private static final String HEADER = "J";

	@Override
	public String getHeader() {
		return HEADER;
	}

	@Override
	public String getArguments() {
		return "";
	}
}
