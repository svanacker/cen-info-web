package org.cen.robot.device.battery.com;

import org.cen.com.out.OutData;

public class BatteryReadOutData extends OutData {
	
	private static final String HEADER = "b";

	@Override
	public String getHeader() {
		return HEADER;
	}
}
