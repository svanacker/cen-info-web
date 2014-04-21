package org.cen.com;

import java.util.Set;

import org.cen.com.documentation.DeviceDataSignature;
import org.cen.com.documentation.DeviceMethodSignature;
import org.cen.com.documentation.DeviceMethodType;
import org.cen.com.documentation.DeviceParameter;
import org.cen.com.documentation.DeviceParameterType;
import org.cen.com.in.InData;
import org.cen.robot.device.navigation.position.com.ReadPositionPulseInData;
import org.junit.Assert;
import org.junit.Test;

/**
 * @see DefaultDecoder
 */
public class DefaultDecoderTest {

	@DeviceDataSignature(deviceName = "SampleDevice", methods = {
			@DeviceMethodSignature(header = "d", type = DeviceMethodType.OUTPUT, parameters = {
					@DeviceParameter(name = "left", length = 4, type = DeviceParameterType.SIGNED, unit = "mm"),
					@DeviceParameter(name = "-", length = 1, type = DeviceParameterType.UNSPECIFIED, unit = ""),
					@DeviceParameter(name = "right", length = ReadPositionPulseInData.LENGTH_RIGHT,
							type = DeviceParameterType.SIGNED, unit = "mm") }),
			@DeviceMethodSignature(header = "e", type = DeviceMethodType.OUTPUT, parameters = {
					@DeviceParameter(name = "status", length = 2, type = DeviceParameterType.SIGNED, unit = "(0,1,2)"),
					@DeviceParameter(name = "-", length = 1, type = DeviceParameterType.UNSPECIFIED, unit = ""),
					@DeviceParameter(name = "x", length = 4, type = DeviceParameterType.SIGNED, unit = "mm"),
					@DeviceParameter(name = "-", length = 1, type = DeviceParameterType.UNSPECIFIED, unit = ""),
					@DeviceParameter(name = "y", length = 4, type = DeviceParameterType.SIGNED, unit = "mm"),
					@DeviceParameter(name = "-", length = 1, type = DeviceParameterType.UNSPECIFIED, unit = ""),
					@DeviceParameter(name = "theta", length = 4, type = DeviceParameterType.SIGNED, unit = "°") }),
			@DeviceMethodSignature(header = "b", type = DeviceMethodType.OUTPUT, parameters = {
					@DeviceParameter(name = "x", length = 4, type = DeviceParameterType.SIGNED, unit = "mm"),
					@DeviceParameter(name = "-", length = 1, type = DeviceParameterType.UNSPECIFIED, unit = ""),
					@DeviceParameter(name = "y", length = 4, type = DeviceParameterType.SIGNED, unit = "mm"),
					@DeviceParameter(name = "-", length = 1, type = DeviceParameterType.UNSPECIFIED, unit = ""),
					@DeviceParameter(name = "theta", length = 4, type = DeviceParameterType.SIGNED, unit = "°") }),
			@DeviceMethodSignature(header = "z", type = DeviceMethodType.OUTPUT, parameters = {
					@DeviceParameter(name = "alpha", length = 4, type = DeviceParameterType.SIGNED, unit = "°"),
					@DeviceParameter(name = "-", length = 1, type = DeviceParameterType.UNSPECIFIED, unit = ""),
					@DeviceParameter(name = "theta", length = 4, type = DeviceParameterType.SIGNED, unit = "°") }) })
	class SampleDecoder extends DefaultDecoder {

		@Override
		public Set<String> getHandledHeaders() {
			return null;
		}

		@Override
		public InData decode(String data) throws IllegalComDataException {
			return null;
		}
	}

	@Test
	public void should_find_right_length_signature_1() throws IllegalComDataException {
		SampleDecoder decoder = new SampleDecoder();
		int dataLength = decoder.getDataLength("e");

		Assert.assertEquals(18, dataLength);
	}

	@Test
	public void should_find_right_length_signature_2() throws IllegalComDataException {
		SampleDecoder decoder = new SampleDecoder();
		int dataLength = decoder.getDataLength("b");

		Assert.assertEquals(15, dataLength);
	}

	@Test
	public void should_return_0_when_no_associated_decoder() throws IllegalComDataException {
		SampleDecoder decoder = new SampleDecoder();
		int dataLength = decoder.getDataLength("t");

		Assert.assertEquals(0, dataLength);
	}
}
