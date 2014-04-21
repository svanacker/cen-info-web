package org.cen.robot.device.navigation.analysis;

import org.cen.com.ComDataUtils;
import org.cen.com.documentation.DeviceDataSignature;
import org.cen.com.documentation.DeviceMethodSignature;
import org.cen.com.documentation.DeviceMethodType;
import org.cen.com.documentation.DeviceParameter;
import org.cen.com.documentation.DeviceParameterType;
import org.cen.com.out.OutData;
import org.cen.robot.control.PIDInstructionType;
import org.cen.robot.device.navigation.NavigationDevice;

/**
 * OutData to ask the motor board to get current parameters about motion.
 */
@DeviceDataSignature(deviceName = NavigationDevice.NAME, methods = { @DeviceMethodSignature(header = PIDMotionDataInData.HEADER,
		type = DeviceMethodType.INPUT, parameters = { @DeviceParameter(name = "instructionType", length = 1,
				type = DeviceParameterType.SIGNED, unit = ""), }) })
public class PIDMotionDataOutData extends OutData {

	private final PIDInstructionType pidInstructionType;

	public PIDMotionDataOutData(PIDInstructionType pidInstructionType) {
		this.pidInstructionType = pidInstructionType;
	}

	@Override
	public String getArguments() {
		String argumentString = ComDataUtils.format(pidInstructionType.getIndex(), 1);

		return argumentString;
	}

	@Override
	public String getDebugString() {
		return "{instructionType=" + pidInstructionType + "}";
	}

	@Override
	public String getHeader() {
		return PIDMotionDataInData.HEADER;
	}
}