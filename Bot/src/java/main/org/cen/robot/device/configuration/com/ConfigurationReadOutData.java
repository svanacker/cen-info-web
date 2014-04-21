package org.cen.robot.device.configuration.com;

import org.cen.com.documentation.DeviceDataSignature;
import org.cen.com.documentation.DeviceMethodSignature;
import org.cen.com.documentation.DeviceMethodType;
import org.cen.com.out.OutData;
import org.cen.robot.device.configuration.ConfigurationDevice;

/**
 * Serial data for reading the configuration device.
 * 
 * @author Emmanuel ZURMELY
 */
@DeviceDataSignature(deviceName = ConfigurationDevice.NAME, methods = { @DeviceMethodSignature(header = ConfigurationReadOutData.HEADER, type = DeviceMethodType.INPUT, parameters = {}) })
public class ConfigurationReadOutData extends OutData {
	static final String HEADER = "c";

	@Override
	public String getHeader() {
		return HEADER;
	}
}
