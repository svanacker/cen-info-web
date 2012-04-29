package org.cen.robot.device.navigation.com;

import org.cen.com.documentation.DeviceDataSignature;
import org.cen.com.documentation.DeviceMethodSignature;
import org.cen.com.documentation.DeviceMethodType;
import org.cen.com.out.OutData;
import org.cen.robot.device.navigation.NavigationDevice;

@DeviceDataSignature(deviceName = NavigationDevice.NAME, methods = { @DeviceMethodSignature(
		header = CalibrationOutData.HEADER,
		type = DeviceMethodType.INPUT,
		parameters = {}) })
public class CalibrationOutData extends OutData {

	protected static final String HEADER = "@";

	@Override
	public String getHeader() {
		return HEADER;
	}
}
