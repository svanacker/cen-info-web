package org.cen.robot.device.navigation.position.com;

import org.cen.com.documentation.DeviceDataSignature;
import org.cen.com.documentation.DeviceMethodSignature;
import org.cen.com.documentation.DeviceMethodType;
import org.cen.com.out.OutData;
import org.cen.robot.device.navigation.NavigationDevice;

/**
 * The data which ask the position in pulse to the micro-controller.
 */
@DeviceDataSignature(deviceName = NavigationDevice.NAME, methods = { @DeviceMethodSignature(
		header = ReadPositionPulseOutData.HEADER,
		type = DeviceMethodType.INPUT,
		parameters = {}) })
public class ReadPositionPulseOutData extends OutData {

	public final static String HEADER = "w";

	/**
	 * Creates an object corresponding to the position
	 */
	public ReadPositionPulseOutData() {
		super();
	}

	@Override
	public String getHeader() {
		return HEADER;
	}
}
