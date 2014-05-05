package org.cen.robot.device.navigation.analysis.com;

import org.cen.robot.control.MotionInstructionData;
import org.cen.robot.control.MotionProfileType;
import org.cen.robot.control.PIDMotionType;
import org.cen.robot.control.PIDType;
import org.cen.robot.device.navigation.analysis.MotionAnalysisDecoder;
import org.cen.robot.device.navigation.analysis.MotionInstructionInData;
import org.cen.robot.device.navigation.analysis.PIDMotionDataInData;
import org.junit.Assert;
import org.junit.Test;

/**
 * @see MotionAnalysisDecoder
 */
public class MotionAnalysisDecoderTest {

	@Test
	public void should_decode_motion_instruction_data_test1() throws Exception {
		MotionAnalysisDecoder decoder = new MotionAnalysisDecoder();

		MotionInstructionInData inData = (MotionInstructionInData) decoder.decode("_1083040-020100120-0100005000-123");
		MotionInstructionData data = inData.getMotionInstructionInData();

		Assert.assertEquals(1, data.getIndex());
		Assert.assertEquals(8f, data.getA());
		Assert.assertEquals(48f, data.getSpeed());
		Assert.assertEquals(64f, data.getSpeedMax());

		Assert.assertEquals(32f, data.getT1());
		Assert.assertEquals(256f, data.getT2());
		Assert.assertEquals(288f, data.getT3());

		Assert.assertEquals(4096f, data.getP1());
		Assert.assertEquals(20480f, data.getP2());

		Assert.assertEquals(MotionProfileType.TRAPEZE, data.getProfileType());
		Assert.assertEquals(PIDMotionType.MOTION_TYPE_ROTATION_ONE_WHEEL, data.getMotionType());
		Assert.assertEquals(PIDType.ADJUST_DIRECTION, data.getPidType());
	}

	@Test
	public void should_decode_motion_pid_data_test2() throws Exception {
		MotionAnalysisDecoder decoder = new MotionAnalysisDecoder();
		PIDMotionDataInData data = (PIDMotionDataInData) decoder.decode("*11001-00200-5678-40-200050008000");

		Assert.assertEquals(1, data.getIndex());
		Assert.assertEquals(256f, data.getPidTime());
		Assert.assertEquals(1f, data.getPidType());

		Assert.assertEquals(512f, data.getPosition());
		Assert.assertEquals(22136f, data.getErrorDataError());

		Assert.assertEquals(64f, data.getU());

		Assert.assertEquals(8192f, data.getEndInfoTime());
		Assert.assertEquals(20480f, data.getEndInfoAbsDeltaPositionIntegral());
		Assert.assertEquals(32768f, data.getEndInfoUIntegral());
	}
}
