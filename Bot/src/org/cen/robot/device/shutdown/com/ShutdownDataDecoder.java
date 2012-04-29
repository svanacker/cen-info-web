package org.cen.robot.device.shutdown.com;

import java.util.HashSet;
import java.util.Set;

import org.cen.com.DefaultDecoder;
import org.cen.com.IllegalComDataException;
import org.cen.com.documentation.DeviceDataSignature;
import org.cen.com.documentation.DeviceMethodSignature;
import org.cen.com.documentation.DeviceMethodType;
import org.cen.com.documentation.DeviceParameter;
import org.cen.com.documentation.DeviceParameterType;
import org.cen.com.in.InData;
import org.cen.robot.device.timer.TimerDevice;

/**
 * Decoder which decodes instruction to shutdown the PC.
 */
@DeviceDataSignature(deviceName = TimerDevice.NAME, methods = { @DeviceMethodSignature(header = ShutdownInData.HEADER,
		type = DeviceMethodType.OUTPUT, parameters = { @DeviceParameter(length = 3, type = DeviceParameterType.UNSPECIFIED,
				name = "sequence", unit = "") }) })
public class ShutdownDataDecoder extends DefaultDecoder {

	private final static Set<String> handled = new HashSet<String>();

	public static final String SHUTDOWN_SEQUENCE = "vVwW";

	static {
		handled.add(ShutdownInData.HEADER);
	}

	@Override
	public InData decode(String data) throws IllegalComDataException {
		if (data.substring(0, 1).equals(ShutdownInData.HEADER)) {
			return new ShutdownInData();
		}
		throw new IllegalComDataException();
	}

	@Override
	public Set<String> getHandledHeaders() {
		return handled;
	}
}
