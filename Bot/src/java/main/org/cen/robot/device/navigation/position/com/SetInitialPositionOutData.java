package org.cen.robot.device.navigation.position.com;

import org.cen.com.ComDataUtils;
import org.cen.com.documentation.DeviceDataSignature;
import org.cen.com.documentation.DeviceMethodSignature;
import org.cen.com.documentation.DeviceMethodType;
import org.cen.com.documentation.DeviceParameter;
import org.cen.com.documentation.DeviceParameterType;
import org.cen.com.out.OutData;
import org.cen.math.MathUtils;
import org.cen.robot.device.navigation.NavigationDevice;
import org.cen.robot.device.pid.com.WritePIDOutData;

//@formatter:off
@DeviceDataSignature(
		deviceName = NavigationDevice.NAME,
		methods = {
		        @DeviceMethodSignature(
				header = WritePIDOutData.HEADER,
				methodName = "writePID",
				type = DeviceMethodType.INPUT,
				parameters = {
						@DeviceParameter(name = "x", length = 4, type = DeviceParameterType.UNSIGNED, unit = "mm"),
						@DeviceParameter(name = "-", length = 1, type = DeviceParameterType.UNSIGNED, unit = ""),
						@DeviceParameter(name = "y", length = 4, type = DeviceParameterType.UNSIGNED, unit = "mm"),
						@DeviceParameter(name = "-", length = 1, type = DeviceParameterType.UNSIGNED, unit = ""),
						@DeviceParameter(
								name = "orientation",
								length = 4,
								type = DeviceParameterType.UNSIGNED,
								unit = "mrad") })
		})
//@formatter:on
public class SetInitialPositionOutData extends OutData {

    private static final String HEADER = "K";

    private final double x;

    private final double y;

    private final double orientation;

    public SetInitialPositionOutData(double x, double y, double orientation) {
        super();
        this.x = x;
        this.y = y;
        this.orientation = orientation;
    }

    @Override
    public String getArguments() {
        int xx = (int) x;
        int yy = (int) y;
        int angle = (int) MathUtils.radToDeciDegree(orientation);
        String sx = ComDataUtils.format(xx, 4);
        String sy = ComDataUtils.format(yy, 4);
        String sAngle = ComDataUtils.format(angle, 4);
        return sx + "-" + sy + "-" + sAngle;
    }

    @Override
    public String getHeader() {
        return HEADER;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[x=" + x + ", y=" + y + ", orientation=" + Math.toDegrees(orientation)
                + "°]";
    }
}
