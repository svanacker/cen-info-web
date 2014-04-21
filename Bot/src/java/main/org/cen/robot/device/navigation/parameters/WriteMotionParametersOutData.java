package org.cen.robot.device.navigation.parameters;

import org.cen.com.ComDataUtils;
import org.cen.com.documentation.DeviceDataSignature;
import org.cen.com.documentation.DeviceMethodSignature;
import org.cen.com.documentation.DeviceMethodType;
import org.cen.com.documentation.DeviceParameter;
import org.cen.com.documentation.DeviceParameterType;
import org.cen.com.out.OutData;
import org.cen.robot.device.navigation.NavigationDevice;

/**
 * Message to change the default parameters for the speed and acceleration.
 */
//@formatter:off
@DeviceDataSignature(deviceName = NavigationDevice.NAME, methods = {
        @DeviceMethodSignature(
		header = WriteMotionParametersOutData.HEADER,
		methodName="writeMotion",
		type = DeviceMethodType.INPUT, parameters = {
				@DeviceParameter(name = "motionType", length = 2, type = DeviceParameterType.UNSIGNED, unit = ""),
				@DeviceParameter(name = "acceleration", length = 2, type = DeviceParameterType.UNSIGNED, unit = ""),
				@DeviceParameter(name = "speed", length = 2, type = DeviceParameterType.UNSIGNED, unit = ""), }) })
//@formatter:on
public class WriteMotionParametersOutData extends OutData {

    public static final String HEADER = ":";

    protected MotionParametersData data;

    /**
     * Build an encapsulation of outData from the object model
     */
    public WriteMotionParametersOutData(MotionParametersData data) {
        super();
        this.data = data;
    }

    @Override
    public String getArguments() {
        // First Argument
        String motionTypeString = ComDataUtils.format(data.getMotionType(), 2);
        // Second Argument
        String accelerationString = ComDataUtils.format(data.getAcceleration(), 2);
        // Third Argument
        String speedString = ComDataUtils.format(data.getSpeed(), 2);
        String result = motionTypeString + accelerationString + speedString;

        return result;
    }

    @Override
    public String getHeader() {
        return HEADER;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[data=" + data + "]";
    }
}
