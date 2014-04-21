package org.cen.robot.device.navigation.position.com;

import org.cen.com.documentation.DeviceDataSignature;
import org.cen.com.documentation.DeviceMethodSignature;
import org.cen.com.documentation.DeviceMethodType;
import org.cen.com.out.OutData;
import org.cen.robot.device.navigation.NavigationDevice;

/**
 * Encapsulation of the message which ask for absolute Position.
 */
@DeviceDataSignature(deviceName = NavigationDevice.NAME, methods = { @DeviceMethodSignature(
		header = PositionAskOutData.HEADER,
		methodName = "positionRead",
		type = DeviceMethodType.OUTPUT,
		parameters = {}) })
public class PositionAskOutData extends OutData {

	public final static String HEADER = "h";

	/**
	 * Constructor.
	 */
	public PositionAskOutData() {
		super();
	}

	@Override
	public String getHeader() {
		return HEADER;
	}
}
