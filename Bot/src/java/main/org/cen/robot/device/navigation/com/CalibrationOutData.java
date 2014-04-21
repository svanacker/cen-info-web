package org.cen.robot.device.navigation.com;

import org.cen.com.documentation.DeviceDataSignature;
import org.cen.com.documentation.DeviceMethodSignature;
import org.cen.com.documentation.DeviceMethodType;
import org.cen.com.out.OutData;
import org.cen.robot.device.navigation.NavigationDevice;

/**
 * Do a series of move which enables to do a calibration of wheel values.
 */
//@formatter:off
@DeviceDataSignature(deviceName = NavigationDevice.NAME, methods = {
        @DeviceMethodSignature(
		header = CalibrationOutData.HEADER,
		methodName = "calibrate",
		type = DeviceMethodType.INPUT,
		parameters = {}) })
//@formatter:on
public class CalibrationOutData extends OutData {

    protected static final String HEADER = "@";

    @Override
    public String getHeader() {
        return HEADER;
    }
}
