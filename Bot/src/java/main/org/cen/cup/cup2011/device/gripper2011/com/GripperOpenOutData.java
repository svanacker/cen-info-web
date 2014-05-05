package org.cen.cup.cup2011.device.gripper2011.com;

import org.cen.com.documentation.DeviceDataSignature;
import org.cen.com.documentation.DeviceMethodSignature;
import org.cen.com.documentation.DeviceMethodType;
import org.cen.com.documentation.DeviceParameter;
import org.cen.com.documentation.DeviceParameterType;
import org.cen.com.out.OutData;
import org.cen.robot.device.navigation.INavigationDevice;

//@formatter:off
@DeviceDataSignature(
        deviceName = INavigationDevice.NAME, methods = {
                @DeviceMethodSignature(
		header = GripperOpenOutData.HEADER,
		methodName = "gripper",
		type = DeviceMethodType.INPUT,
		parameters = { @DeviceParameter(name = "index", length = 2, type = DeviceParameterType.SIGNED, unit = ""), }) })
//@formatter:on
public class GripperOpenOutData extends OutData {

    public static final String HEADER = "I";

    @Override
    public String getHeader() {
        return HEADER;
    }

    @Override
    public String getArguments() {
        return "";
    }
}
