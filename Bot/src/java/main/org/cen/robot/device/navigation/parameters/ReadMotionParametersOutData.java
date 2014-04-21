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
@DeviceDataSignature(
        deviceName = NavigationDevice.NAME,
        methods = { 
                @DeviceMethodSignature(
                        header = ReadMotionParametersOutData.HEADER,
                        methodName="readMotionParameter",
                        type = DeviceMethodType.INPUT,
                        parameters = {
                                @DeviceParameter(name = "motionType", length = 2, type = DeviceParameterType.UNSIGNED, unit = "")
                                })
                })
//@formatter:on
public class ReadMotionParametersOutData extends OutData {

    public static final String HEADER = "?";

    private final int motionType;

    /**
     * Build an encapsulation of outData from the object model
     */
    public ReadMotionParametersOutData(int motionType) {
        super();
        this.motionType = motionType;
    }

    @Override
    public String getArguments() {
        // First Argument
        String motionTypeString = ComDataUtils.format(motionType, 2);
        String result = motionTypeString;

        return result;
    }

    @Override
    public String getHeader() {
        return HEADER;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[motionType=" + motionType + "]";
    }
}
