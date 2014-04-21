package org.cen.robot.device.pid.com;

import org.cen.com.in.InData;
import org.cen.robot.control.PIDData;

/**
 * Encapsulation of the message corresponding to the response of the javelin.
 */
public class ReadPIDInData extends InData {

	public static final String HEADER = "q";

	private final PIDData data;

	public ReadPIDInData(PIDData data) {
		super();
		this.data = data;
	}

	public PIDData getData() {
		return data;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{data=" + data + "}";
	}
}
