package org.cen.simulRobot.device.vision.com;

import org.cen.com.out.OutData;

public class VisionSimulReadOutData extends OutData {

	static final String HEADER = "i";

	@Override
	public String getHeader() {
		return HEADER;
	}
}
