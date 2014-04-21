package org.cen.robot.device.navigation.parameters;

import org.cen.com.in.InData;

/**
 * Read parameters for motion.
 */
public class ReadMotionParametersInData extends InData {

	public final static String HEADER = "?";

	public MotionParametersData getData() {
		return data;
	}

	private final MotionParametersData data;

	public ReadMotionParametersInData(MotionParametersData data) {
		super();
		this.data = data;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[data=" + data + "]";
	}
}
