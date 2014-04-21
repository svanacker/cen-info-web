package org.cen.robot.device.navigation.com;

import org.cen.com.ComDataUtils;
import org.cen.com.documentation.DeviceDataSignature;
import org.cen.com.documentation.DeviceMethodSignature;
import org.cen.com.documentation.DeviceMethodType;
import org.cen.com.documentation.DeviceParameter;
import org.cen.com.documentation.DeviceParameterType;
import org.cen.com.out.OutData;
import org.cen.math.MathUtils;
import org.cen.robot.device.navigation.NavigationDevice;

/**
 * Corresponds to the data which is sent to the COM stream to rotate the robot
 * to the left with only one Wheel.
 */
//@formatter:off
@DeviceDataSignature(deviceName = NavigationDevice.NAME, methods = {
        @DeviceMethodSignature(
		header = RotateLeftOneWheelOutData.HEADER,
		methodName = "leftOneWheel",
		type = DeviceMethodType.INPUT,
		parameters = { @DeviceParameter(
				name = "left",
				length = 4,
				type = DeviceParameterType.SIGNED,
				unit = "angle in deciDeg"), 
				})
        })
//@formatter:on
public class RotateLeftOneWheelOutData extends OutData {

    public static final String HEADER = ")";

    /**
     * The angle for rotation.
     */
    protected double angle;

    /**
     * Constructor with all arguments.
     */
    public RotateLeftOneWheelOutData(double angleRad) {
        this.angle = angleRad;
    }

    @Override
    public String getArguments() {
        int deciDegree = (int) MathUtils.radToDeciDegree(angle);
        String argumentString = ComDataUtils.format(deciDegree, 4);

        return argumentString;
    }

    @Override
    public String getDebugString() {
        return "{angle=" + MathUtils.radToDeciDegree(angle) + "°}";
    }

    @Override
    public String getHeader() {
        return HEADER;
    }
}
