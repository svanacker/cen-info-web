package org.cen.robot.control.graph.sample;

import org.cen.robot.control.MotionInstructionData;
import org.cen.robot.control.MotionProfileType;
import org.cen.robot.control.PIDType;

/**
 * Sample Data to test JFreechart.
 */
public class MotionInstructionDataSample {

	/**
	 * Create sample Data for a fake go move for theta pid.
	 * 
	 * @return
	 */
	public static MotionInstructionData createGoThetaSample() {
		MotionInstructionData result = new MotionInstructionData();
		result.setIndex(1);
		result.setProfileType(MotionProfileType.TRAPEZE);
		result.setPidType(PIDType.GO);
		result.setSpeed(20f);
		result.setSpeedMax(20f);
		result.setA(8.0f);
		result.setP1(200.0f);
		result.setP2(1000.0f);
		result.setNextPosition(1200.0f);
		result.setT1(24);
		result.setT2(100);
		result.setT3(result.getT1() + result.getT2());

		return result;
	}

	/**
	 * Create sample Data for a fake go move for alpha pid.
	 * 
	 * @return
	 */
	public static MotionInstructionData createGoAlphaSample() {
		MotionInstructionData result = new MotionInstructionData();

		result.setProfileType(MotionProfileType.TRAPEZE);
		result.setPidType(PIDType.GO);

		// All others values egal to 0, because we don't do any rotation

		return result;
	}
}
