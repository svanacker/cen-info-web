package org.cen.robot.device.pid.com;

import org.cen.com.ComDataUtils;
import org.cen.com.documentation.DeviceDataSignature;
import org.cen.com.documentation.DeviceMethodSignature;
import org.cen.com.documentation.DeviceMethodType;
import org.cen.com.documentation.DeviceParameter;
import org.cen.com.documentation.DeviceParameterType;
import org.cen.com.out.OutData;
import org.cen.robot.device.navigation.NavigationDevice;

@DeviceDataSignature(deviceName = NavigationDevice.NAME, methods = { @DeviceMethodSignature(
		header = WriteMotionEndDetectionParameterOutData.HEADER, type = DeviceMethodType.INPUT, parameters = {
				@DeviceParameter(name = "absDeltaPositionIntegralFactorThreshold", length = 2,
						type = DeviceParameterType.UNSIGNED, unit = ""),
				@DeviceParameter(name = "maxUIntegralFactorThreshold", length = 2, type = DeviceParameterType.UNSIGNED,
						unit = ""),
				@DeviceParameter(name = "maxUIntegralConstantThreshold", length = 2,
						type = DeviceParameterType.UNSIGNED, unit = ""),
				@DeviceParameter(name = "timeRangeAnalysis", length = 2, type = DeviceParameterType.UNSIGNED,
						unit = " pidTime"),
				@DeviceParameter(name = "noAnalysisAtStartupTime", length = 2, type = DeviceParameterType.UNSIGNED,
						unit = "pidTime") }) })
public class WriteMotionEndDetectionParameterOutData extends OutData {

	final static String HEADER = "=";

	public MotionEndDetectionParameter getEndDetectionParameter() {
		return endDetectionParameter;
	}

	private final MotionEndDetectionParameter endDetectionParameter;

	/**
	 * Constructor.
	 */
	public WriteMotionEndDetectionParameterOutData(MotionEndDetectionParameter endDetectionParameter) {
		super();
		this.endDetectionParameter = endDetectionParameter;
	}

	@Override
	public String getArguments() {
		String result = ComDataUtils
				.format((int) endDetectionParameter.getAbsDeltaPositionIntegralFactorThreshold(), 2);
		result += ComDataUtils.format((int) endDetectionParameter.getMaxUIntegralFactorThreshold(), 2);
		result += ComDataUtils.format((int) endDetectionParameter.getMaxUIntegralConstantThreshold(), 2);
		result += ComDataUtils.format(endDetectionParameter.getTimeRangeAnalysis(), 2);
		result += ComDataUtils.format(endDetectionParameter.getNoAnalysisAtStartupTime(), 2);
		return result;
	}

	@Override
	public String getHeader() {
		return HEADER;
	}
}
