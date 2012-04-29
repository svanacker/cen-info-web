package org.cen.simulRobot.device.com;

import org.cen.com.out.OutData;

public class AckSimulOutData extends OutData {

	static final String HEADER = "a";
	protected String deviceHeader;

	public AckSimulOutData(String pdeviceHeader){
		this.deviceHeader = pdeviceHeader;
	}

	@Override
	public String getArguments() {
		return null;
	}

	public String getDeviceHeader() {
		return deviceHeader;
	}

	@Override
	public String getHeader() {
		return HEADER;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[header=" + HEADER + " deviceHeader=" + deviceHeader + "]";
	}
}
