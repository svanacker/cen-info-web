package org.cen.robot.device.navigation.com;

import java.util.HashSet;
import java.util.Set;

import org.cen.com.ComDataUtils;
import org.cen.com.DefaultDecoder;
import org.cen.com.IllegalComDataException;
import org.cen.com.documentation.DeviceDataSignature;
import org.cen.com.documentation.DeviceMethodSignature;
import org.cen.com.documentation.DeviceMethodType;
import org.cen.com.documentation.DeviceParameter;
import org.cen.com.documentation.DeviceParameterType;
import org.cen.com.in.InData;
import org.cen.com.in.UntypedInData;
import org.cen.robot.control.PIDData;
import org.cen.robot.control.PIDInstructionType;
import org.cen.robot.control.PIDType;
import org.cen.robot.control.RobotControlEngine;
import org.cen.robot.device.navigation.NavigationDevice;
import org.cen.robot.device.navigation.parameters.MotionParametersData;
import org.cen.robot.device.navigation.parameters.ReadMotionParametersInData;
import org.cen.robot.device.navigation.position.com.PositionAskInData;
import org.cen.robot.device.navigation.position.com.PositionInData;
import org.cen.robot.device.navigation.position.com.ReadPositionPulseInData;
import org.cen.robot.device.pid.com.MotionEndDetectionParameter;
import org.cen.robot.device.pid.com.ReadMotionEndDetectionParameterInData;
import org.cen.robot.device.pid.com.ReadPIDInData;

/**
 * Decoder for serial data intended for the navigation device.
 * 
 * @author Emmanuel ZURMELY
 */
@DeviceDataSignature(deviceName = NavigationDevice.NAME, methods = {
		@DeviceMethodSignature(header = ReadPositionPulseInData.HEADER, type = DeviceMethodType.OUTPUT, parameters = {
				@DeviceParameter(name = "left", length = ReadPositionPulseInData.LENGTH_LEFT, type = DeviceParameterType.SIGNED,
						unit = "mm"),
				@DeviceParameter(name = "-", length = 1, type = DeviceParameterType.UNSPECIFIED, unit = ""),
				@DeviceParameter(name = "right", length = ReadPositionPulseInData.LENGTH_RIGHT,
						type = DeviceParameterType.SIGNED, unit = "mm") }),
		@DeviceMethodSignature(header = PositionInData.HEADER, type = DeviceMethodType.OUTPUT, parameters = {
				@DeviceParameter(name = "status", length = 2, type = DeviceParameterType.SIGNED, unit = "(0,1,2)"),
				@DeviceParameter(name = "-", length = 1, type = DeviceParameterType.UNSPECIFIED, unit = ""),
				@DeviceParameter(name = "x", length = 4, type = DeviceParameterType.SIGNED, unit = "mm"),
				@DeviceParameter(name = "-", length = 1, type = DeviceParameterType.UNSPECIFIED, unit = ""),
				@DeviceParameter(name = "y", length = 4, type = DeviceParameterType.SIGNED, unit = "mm"),
				@DeviceParameter(name = "-", length = 1, type = DeviceParameterType.UNSPECIFIED, unit = ""),
				@DeviceParameter(name = "theta", length = 4, type = DeviceParameterType.SIGNED, unit = "°") }),
		@DeviceMethodSignature(header = PositionAskInData.HEADER, type = DeviceMethodType.OUTPUT, parameters = {
				@DeviceParameter(name = "x", length = 4, type = DeviceParameterType.SIGNED, unit = "mm"),
				@DeviceParameter(name = "-", length = 1, type = DeviceParameterType.UNSPECIFIED, unit = ""),
				@DeviceParameter(name = "y", length = 4, type = DeviceParameterType.SIGNED, unit = "mm"),
				@DeviceParameter(name = "-", length = 1, type = DeviceParameterType.UNSPECIFIED, unit = ""),
				@DeviceParameter(name = "theta", length = 4, type = DeviceParameterType.SIGNED, unit = "°") }),
		@DeviceMethodSignature(header = ReadPIDInData.HEADER, type = DeviceMethodType.OUTPUT, parameters = {
				@DeviceParameter(name = "index", length = 2, type = DeviceParameterType.SIGNED, unit = ""),
				@DeviceParameter(name = "p", length = 2, type = DeviceParameterType.SIGNED, unit = ""),
				@DeviceParameter(name = "i", length = 2, type = DeviceParameterType.SIGNED, unit = ""),
				@DeviceParameter(name = "d", length = 2, type = DeviceParameterType.SIGNED, unit = ""),
				@DeviceParameter(name = "Imax", length = 2, type = DeviceParameterType.SIGNED, unit = "") }),
		@DeviceMethodSignature(header = ReadMotionParametersInData.HEADER, type = DeviceMethodType.OUTPUT, parameters = {
				@DeviceParameter(name = "motionType", length = 2, type = DeviceParameterType.SIGNED, unit = ""),
				@DeviceParameter(name = "acceleration", length = 2, type = DeviceParameterType.UNSIGNED, unit = ""),
				@DeviceParameter(name = "speed", length = 2, type = DeviceParameterType.UNSIGNED, unit = "") }),
		@DeviceMethodSignature(header = ReadMotionEndDetectionParameterInData.HEADER, type = DeviceMethodType.OUTPUT,
				parameters = {
						@DeviceParameter(name = "absDeltaPositionIntegralFactorThreshold", length = 2,
								type = DeviceParameterType.SIGNED, unit = ""),
						@DeviceParameter(name = "maxUIntegralFactorThreshold", length = 2, type = DeviceParameterType.UNSIGNED,
								unit = ""),
						@DeviceParameter(name = "maxUIntegralConstantThreshold", length = 2, type = DeviceParameterType.UNSIGNED,
								unit = ""),
						@DeviceParameter(name = "timeRangeAnalysis", length = 2, type = DeviceParameterType.UNSIGNED, unit = ""),
						@DeviceParameter(name = "noAnalysisAtStartupTime", length = 2, type = DeviceParameterType.UNSIGNED,
								unit = ""), })

})
public class NavigationDataDecoder extends DefaultDecoder {

	public final static Set<String> handled = new HashSet<String>();

	static {
		handled.add(ReadPositionPulseInData.HEADER);
		handled.add(ReadPIDInData.HEADER);
		handled.add(PositionInData.HEADER);
		handled.add(PositionAskInData.HEADER);
		handled.add(ReadMotionParametersInData.HEADER);
		handled.add(ReadMotionEndDetectionParameterInData.HEADER);
	}

	@Override
	public InData decode(String data) throws IllegalComDataException {
		String header = data.substring(0, 1);
		if (header.equals(ReadPositionPulseInData.HEADER)) {
			checkLength(header, data);
			return decodePositionData(data);
		} else if (header.equals(ReadPIDInData.HEADER)) {
			checkLength(header, data);
			return decodePID(data);
		} else if (header.equals(PositionInData.HEADER)) {
			checkLength(header, data);
			return decodePosition(data);
		} else if (header.equals(PositionAskInData.HEADER)) {
			checkLength(header, data);
			return decodeAskPosition(data);
		} else if (header.equals(ReadMotionParametersInData.HEADER)) {
			checkLength(header, data);
			return decodeMotionParameters(data);
		} else if (header.equals(ReadMotionEndDetectionParameterInData.HEADER)) {
			checkLength(header, data);
			return decodeEndMotionParameters(data);
		}
		return new UntypedInData(data);
	}

	private ReadMotionParametersInData decodeMotionParameters(String data) throws IllegalComDataException {
		int motionType = (int) ComDataUtils.parseIntHex(data.substring(1, 3));
		int acceleration = (int) ComDataUtils.parseIntHex(data.substring(3, 5));
		int speed = (int) ComDataUtils.parseIntHex(data.substring(5, 7));
		MotionParametersData motionParametersData = new MotionParametersData(motionType, acceleration, speed);
		ReadMotionParametersInData result = new ReadMotionParametersInData(motionParametersData);

		return result;
	}

	private ReadMotionEndDetectionParameterInData decodeEndMotionParameters(String data) throws IllegalComDataException {
		float absDeltaPositionIntegralFactorThreshold = ComDataUtils.parseIntHex(data.substring(1, 3));
		float maxUIntegralFactorThreshold = ComDataUtils.parseIntHex(data.substring(3, 5));
		float maxUIntegralConstantThreshold = ComDataUtils.parseIntHex(data.substring(5, 7));
		int timeRangeAnalysis = (int) ComDataUtils.parseIntHex(data.substring(7, 9));
		int noAnalysisAtStartupTime = (int) ComDataUtils.parseIntHex(data.substring(9, 11));

		MotionEndDetectionParameter parameter = new MotionEndDetectionParameter(absDeltaPositionIntegralFactorThreshold,
				maxUIntegralFactorThreshold, maxUIntegralConstantThreshold, timeRangeAnalysis, noAnalysisAtStartupTime);

		ReadMotionEndDetectionParameterInData result = new ReadMotionEndDetectionParameterInData(parameter);

		return result;
	}

	private ReadPIDInData decodePID(String data) throws IllegalComDataException {
		PIDData pidData = readPID(data);

		ReadPIDInData result = new ReadPIDInData(pidData);
		int index = pidData.getIndex();
		PIDInstructionType instructionType = RobotControlEngine.getInstructionType(index);
		PIDType pidType = RobotControlEngine.getType(index);
		pidData.setInstructionType(instructionType);
		pidData.setPidType(pidType);

		return result;
	}

	private PositionInData decodePosition(String data) throws IllegalComDataException {
		byte status = ComDataUtils.parseByteHex(data.substring(1, 3));
		long x = ComDataUtils.parseShortHex(data.substring(4, 8));
		long y = ComDataUtils.parseShortHex(data.substring(9, 13));
		long theta = ComDataUtils.parseShortHex(data.substring(14, 18));
		return new PositionInData(x, y, theta, status);
	}

	private PositionAskInData decodeAskPosition(String data) throws IllegalComDataException {
		long x = ComDataUtils.parseShortHex(data.substring(1, 5));
		long y = ComDataUtils.parseShortHex(data.substring(6, 10));
		long theta = ComDataUtils.parseShortHex(data.substring(11, 15));
		return new PositionAskInData(x, y, theta);
	}

	private ReadPositionPulseInData decodePositionData(String data) throws IllegalComDataException {
		long left = ComDataUtils.parseIntHex(data.substring(1, 9));
		long right = ComDataUtils.parseIntHex(data.substring(10, 18));
		return new ReadPositionPulseInData(left, right);
	}

	private PIDData readPID(String data) {
		PIDData e = new PIDData();
		e.setIndex((int) ComDataUtils.parseIntHex(data.substring(1, 3)));
		e.setP((int) ComDataUtils.parseIntHex(data.substring(3, 5)));
		e.setI((int) ComDataUtils.parseIntHex(data.substring(5, 7)));
		e.setD((int) ComDataUtils.parseIntHex(data.substring(7, 9)));
		e.setMaxI((int) ComDataUtils.parseIntHex(data.substring(9, 11)));
		return e;
	}

	@Override
	public Set<String> getHandledHeaders() {
		return handled;
	}
}
