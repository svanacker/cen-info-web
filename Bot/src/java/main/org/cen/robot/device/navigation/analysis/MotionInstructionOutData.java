package org.cen.robot.device.navigation.analysis;

import org.cen.com.documentation.DeviceDataSignature;
import org.cen.com.documentation.DeviceMethodSignature;
import org.cen.com.documentation.DeviceMethodType;
import org.cen.com.documentation.DeviceParameter;
import org.cen.com.documentation.DeviceParameterType;
import org.cen.com.out.OutData;
import org.cen.com.utils.ComDataUtils;
import org.cen.robot.control.PIDInstructionType;
import org.cen.robot.device.navigation.INavigationDevice;

/**
 * OutData to ask the motor board to get current parameters about motion.
 */
//@formatter:off
@DeviceDataSignature(
        deviceName = INavigationDevice.NAME,
        methods = { 
                @DeviceMethodSignature(
                        header = MotionInstructionInData.HEADER,
                        methodName = "motion",
                        type = DeviceMethodType.INPUT,
                        parameters = {
                                @DeviceParameter(name = "instructionType", length = 1, type = DeviceParameterType.SIGNED, unit = ""), 
                                })
                })
//@formatter:off
public class MotionInstructionOutData extends OutData {

	private final PIDInstructionType pidInstructionType;

	public MotionInstructionOutData(PIDInstructionType pidInstructionType) {
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
		return MotionInstructionInData.HEADER;
	}
}
