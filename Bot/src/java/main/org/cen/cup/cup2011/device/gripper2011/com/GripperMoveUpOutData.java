package org.cen.cup.cup2011.device.gripper2011.com;

import org.cen.com.out.OutData;

public class GripperMoveUpOutData extends OutData {
	private static final String HEADER = "U";

	@Override
	public String getHeader() {
		return HEADER;
	}
}
