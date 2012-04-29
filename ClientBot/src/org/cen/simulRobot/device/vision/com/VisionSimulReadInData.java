package org.cen.simulRobot.device.vision.com;

import org.cen.com.in.InData;

/**
 * Data which comes from the client and contains the configuration.
 */
public class VisionSimulReadInData extends InData {
	/** The message header which is sent by the client. */
	static final String HEADER = "i";


	public VisionSimulReadInData() {
		super();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[header=" + HEADER + "]";
	}
}
