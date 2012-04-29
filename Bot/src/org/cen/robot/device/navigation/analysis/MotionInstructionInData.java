package org.cen.robot.device.navigation.analysis;

import org.cen.com.in.InData;
import org.cen.robot.control.MotionInstructionData;

/**
 * Data which read the motion instruction provide.
 */
public class MotionInstructionInData extends InData {

	public static final String HEADER = "_";

	protected MotionInstructionData data;

	public MotionInstructionInData(MotionInstructionData data) {
		this.data = data;
	}

	public MotionInstructionData getMotionInstructionInData() {
		return data;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[data=" + data + "]";
	}
}
