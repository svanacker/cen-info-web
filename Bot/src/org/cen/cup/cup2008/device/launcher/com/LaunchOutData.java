package org.cen.cup.cup2008.device.launcher.com;

import org.cen.com.out.OutData;

public class LaunchOutData extends OutData {
	private static final String HEADER = "I";

	@Override
	public String getHeader() {
		return HEADER;
	}
}
