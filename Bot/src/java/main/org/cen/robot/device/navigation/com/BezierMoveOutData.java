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
 * Motion data of a Bezier curve.
 * 
 * @author Emmanuel ZURMELY
 */
@DeviceDataSignature(deviceName = NavigationDevice.NAME, methods = { @DeviceMethodSignature(header = BezierMoveOutData.HEADER, type = DeviceMethodType.INPUT, parameters = { @DeviceParameter(name = "x", length = 4, type = DeviceParameterType.SIGNED, unit = "mm"), @DeviceParameter(name = "-", length = 1, type = DeviceParameterType.UNSPECIFIED, unit = ""), @DeviceParameter(name = "y", length = 4, type = DeviceParameterType.SIGNED, unit = "mm"), @DeviceParameter(name = "-", length = 1, type = DeviceParameterType.UNSPECIFIED, unit = ""), @DeviceParameter(name = "angle", length = 4, type = DeviceParameterType.SIGNED, unit = "rad"), @DeviceParameter(name = "-", length = 1, type = DeviceParameterType.UNSPECIFIED, unit = ""),
		@DeviceParameter(name = "control distance P0-P1", length = 2, type = DeviceParameterType.SIGNED, unit = "cm"), @DeviceParameter(name = "control distance P2-P3", length = 2, type = DeviceParameterType.SIGNED, unit = "cm") }) })
public class BezierMoveOutData extends OutData {

	protected static final String HEADER = "&";

	private final double x;

	private final double y;

	private final double angle;

	private final double d1;

	private final double d2;

	public BezierMoveOutData(double x, double y, double angle, double d1, double d2) {
		super();
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.d1 = d1;
		this.d2 = d2;
	}

	@Override
	public String getArguments() {
		String xStr = ComDataUtils.format((int) x, 4);
		String yStr = ComDataUtils.format((int) y, 4);
		// conversion from radians to deci-degrees
		int deciDegrees = (int) MathUtils.radToDeciDegree(angle);
		String aStr = ComDataUtils.format(deciDegrees, 4);
		String dStr1 = ComDataUtils.format((int) d1, 2);
		String dStr2 = ComDataUtils.format((int) d2, 2);

		return xStr + "-" + yStr + "-" + aStr + "-" + dStr1 + "-" + dStr2;
	}

	@Override
	public String getHeader() {
		return HEADER;
	}
}
