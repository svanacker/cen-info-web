package org.cen.cup.cup2011.device.gripper2011.com;

import org.cen.com.out.OutData;

public class GetKingPresenceOutData extends OutData {
	@Override
	public String getHeader() {
		return ">";
	}

	@Override
	public String getArguments() {
		return "00";
	}
}
