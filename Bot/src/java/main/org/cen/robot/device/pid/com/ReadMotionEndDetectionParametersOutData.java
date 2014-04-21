package org.cen.robot.device.pid.com;

import org.cen.com.documentation.DeviceDataSignature;
import org.cen.com.documentation.DeviceMethodSignature;
import org.cen.com.documentation.DeviceMethodType;
import org.cen.com.out.OutData;
import org.cen.robot.device.navigation.NavigationDevice;

@DeviceDataSignature(deviceName = NavigationDevice.NAME, methods = { @DeviceMethodSignature(
		header = WriteMotionEndDetectionParameterOutData.HEADER, type = DeviceMethodType.INPUT, parameters = {}) })
public class ReadMotionEndDetectionParametersOutData extends OutData {

	final static String HEADER = "~";

	/**
	 * Constructor.
	 */
	public ReadMotionEndDetectionParametersOutData() {
		super();
	}

	@Override
	public String getHeader() {
		return HEADER;
	}
}