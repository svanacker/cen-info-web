package org.cen.robot.device.pid.com;

import org.cen.com.in.InData;

public class ReadMotionEndDetectionParameterInData extends InData {

	public static final String HEADER = "~";

	private final MotionEndDetectionParameter data;

	public ReadMotionEndDetectionParameterInData(MotionEndDetectionParameter data) {
		super();
		this.data = data;
	}

	public MotionEndDetectionParameter getData() {
		return data;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{data=" + data + "}";
	}
}
