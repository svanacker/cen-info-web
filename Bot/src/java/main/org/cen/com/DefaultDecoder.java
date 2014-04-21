package org.cen.com;

import org.cen.com.documentation.DeviceDataSignature;
import org.cen.com.documentation.DeviceMethodSignature;
import org.cen.com.documentation.DeviceParameter;

/**
 * Default implementation of Decoder to manage check of length, default length
 * and other common stuff.
 */
public abstract class DefaultDecoder implements InDataDecoder {

	protected void checkLength(String header, String data) throws IllegalComDataException {
		int dataLength = getDataLength(header);
		checkLength(data, dataLength);
	}

	private void checkLength(String data, int expectedLength) throws IllegalComDataException {
		int dataLength = data.length();
		if (dataLength != expectedLength) {
			throw new IllegalComDataLengthException(expectedLength, dataLength);
		}
	}

	private DeviceDataSignature getDataSignature() {
		DeviceDataSignature result = getClass().getAnnotation(DeviceDataSignature.class);

		return result;
	}

	private DeviceMethodSignature getMethodSignature(String header) {
		DeviceDataSignature dataSignature = getDataSignature();
		DeviceMethodSignature[] methods = dataSignature.methods();

		for (DeviceMethodSignature methodSignature : methods) {
			String methodHeader = methodSignature.header();
			if (header.equals(methodHeader)) {
				return methodSignature;
			}
		}
		return null;
	}

	private int getDataLength(DeviceMethodSignature methodSignature) {
		int result = 0;
		if (methodSignature == null) {
			return result;
		}
		DeviceParameter[] parameters = methodSignature.parameters();
		for (DeviceParameter parameter : parameters) {
			int parameterLength = parameter.length();
			result += parameterLength;
		}
		// we include the length of the header (char)
		return result + 1;
	}

	@Override
	public int getDataLength(String header) {
		DeviceMethodSignature methodSignature = getMethodSignature(header);

		int result = getDataLength(methodSignature);

		return result;
	}
}
