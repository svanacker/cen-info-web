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
@DeviceDataSignature(deviceName = NavigationDevice.NAME, methods = { @DeviceMethodSignature(
		header = RightCircleOutData.HEADER,
		methodName = "rightCircle",
		type = DeviceMethodType.INPUT,
		parameters = {
				@DeviceParameter(
						name = "left",
						length = 4,
						type = DeviceParameterType.SIGNED,
						unit = "angle in deciDeg"),
				@DeviceParameter(name = "radius", length = 4, type = DeviceParameterType.SIGNED, unit = "mm"),

		}) })
//@formatter:on
public class RightCircleOutData extends OutData {

    public static final String HEADER = "{";

    /**
     * The angle for rotation.
     */
    protected double angleRad;

    /**
     * The distance to the center
     */
    protected int radius;

    /**
     * Constructor with all arguments.
     */
    public RightCircleOutData(double angleRad) {
        this.angleRad = angleRad;
    }

    @Override
    public String getArguments() {
        int deciDegree = (int) MathUtils.radToDeciDegree(angleRad);
        String result = ComDataUtils.format(deciDegree, 4);

        result += ComDataUtils.format(radius, 4);

        return result;
    }

    @Override
    public String getDebugString() {
        return "{angle=" + MathUtils.radToDeciDegree(angleRad) + "°, radius=" + radius + " mm}";
    }

    @Override
    public String getHeader() {
        return HEADER;
    }
}
