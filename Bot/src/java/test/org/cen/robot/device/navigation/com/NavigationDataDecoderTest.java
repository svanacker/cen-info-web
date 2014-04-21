package org.cen.robot.device.navigation.com;

import org.cen.com.IllegalComDataException;
import org.cen.robot.control.PIDData;
import org.cen.robot.device.navigation.parameters.MotionParametersData;
import org.cen.robot.device.navigation.parameters.ReadMotionParametersInData;
import org.cen.robot.device.navigation.position.com.PositionAskInData;
import org.cen.robot.device.navigation.position.com.PositionInData;
import org.cen.robot.device.navigation.position.com.PositionStatus;
import org.cen.robot.device.navigation.position.com.ReadPositionPulseInData;
import org.cen.robot.device.pid.com.MotionEndDetectionParameter;
import org.cen.robot.device.pid.com.ReadMotionEndDetectionParameterInData;
import org.cen.robot.device.pid.com.ReadPIDInData;
import org.junit.Assert;
import org.junit.Test;

/**
 * @see NavigationDataDecoder
 */
public class NavigationDataDecoderTest {

	// ReadPositionPulseInData

	@Test
	public void should_decode_position_pulse_test1() throws Exception {
		NavigationDataDecoder decoder = new NavigationDataDecoder();

		ReadPositionPulseInData inData = (ReadPositionPulseInData) decoder.decode("w00AB1234-00CD5678");

		long left = inData.getLeft();
		long right = inData.getRight();

		Assert.assertTrue(11211316 == left);
		Assert.assertTrue(13457016 == right);
	}

	@Test
	public void should_decode_position_pulse_test2() throws Exception {
		NavigationDataDecoder decoder = new NavigationDataDecoder();

		ReadPositionPulseInData inData = (ReadPositionPulseInData) decoder.decode("wFFAB1234-FFCD5678");

		long left = inData.getLeft();
		long right = inData.getRight();

		Assert.assertTrue(-5565900 == left);
		Assert.assertTrue(-3320200 == right);
	}

	@Test
	public void should_decode_position_pulse_test3() throws Exception {
		NavigationDataDecoder decoder = new NavigationDataDecoder();

		ReadPositionPulseInData inData = (ReadPositionPulseInData) decoder.decode("wFFFFFFFF-FFFFFFFF");

		long left = inData.getLeft();
		long right = inData.getRight();

		Assert.assertTrue(-1 == left);
		Assert.assertTrue(-1 == right);
	}

	@Test(expected = IllegalComDataException.class)
	public void should_throws_exception() throws Exception {
		NavigationDataDecoder decoder = new NavigationDataDecoder();

		decoder.decode("w0000-NOT-ENOUGH");
	}

	@Test
	public void should_decode_absolute_position_test1() throws Exception {
		NavigationDataDecoder decoder = new NavigationDataDecoder();

		PositionInData inData = (PositionInData) decoder.decode("k02-0123-4567-0384");

		PositionStatus status = inData.getStatus();
		double x = inData.getX();
		double y = inData.getY();

		double alpha = inData.getAlpha();

		Assert.assertEquals(PositionStatus.BLOCKED, status);
		Assert.assertEquals(291d, x);
		Assert.assertEquals(17767d, y);
		Assert.assertEquals(Math.PI / 2, alpha);
	}

	@Test
	public void should_decode_ask_position_test1() throws IllegalComDataException {
		NavigationDataDecoder decoder = new NavigationDataDecoder();

		PositionAskInData inData = (PositionAskInData) decoder.decode("h0123-4567-0708");
		double x = inData.getX();
		double y = inData.getY();

		double alpha = inData.getAlpha();

		Assert.assertEquals(291d, x);
		Assert.assertEquals(17767d, y);
		Assert.assertEquals(Math.PI, alpha);
	}

	@Test
	public void should_decode_read_pid() throws IllegalComDataException {
		NavigationDataDecoder decoder = new NavigationDataDecoder();

		ReadPIDInData inData = (ReadPIDInData) decoder.decode("q0102030405");
		PIDData data = inData.getData();
		Assert.assertEquals(1, data.getIndex());
		Assert.assertEquals(2, data.getP());
		Assert.assertEquals(3, data.getI());
		Assert.assertEquals(4, data.getD());
		Assert.assertEquals(5, data.getMaxI());
	}

	@Test
	public void should_decode_motion_parameter_test1() throws IllegalComDataException {
		NavigationDataDecoder decoder = new NavigationDataDecoder();

		ReadMotionParametersInData inData = (ReadMotionParametersInData) decoder.decode("?113325");
		MotionParametersData data = inData.getData();
		int motionType = data.getMotionType();
		int acceleration = data.getAcceleration();
		int speed = data.getSpeed();

		Assert.assertEquals(17, motionType);
		Assert.assertEquals(51, acceleration);
		Assert.assertEquals(37, speed);
	}

	@Test
	public void should_decode_motion_end_detection_parameter_test1() throws IllegalComDataException {
		NavigationDataDecoder decoder = new NavigationDataDecoder();

		ReadMotionEndDetectionParameterInData inData = (ReadMotionEndDetectionParameterInData) decoder.decode("~0102030405");
		MotionEndDetectionParameter data = inData.getData();

		Assert.assertEquals(1.0f, data.getAbsDeltaPositionIntegralFactorThreshold());
		Assert.assertEquals(2.0f, data.getMaxUIntegralFactorThreshold());
		Assert.assertEquals(3.0f, data.getMaxUIntegralConstantThreshold());
		Assert.assertEquals(4, data.getTimeRangeAnalysis());
		Assert.assertEquals(5, data.getNoAnalysisAtStartupTime());
	}
}
