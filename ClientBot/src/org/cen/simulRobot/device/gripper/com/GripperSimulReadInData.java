package org.cen.simulRobot.device.gripper.com;

import org.cen.com.in.InData;

/**
 * Data which comes from the client and contains the configuration.
 */
public abstract class GripperSimulReadInData extends InData {
	/** The message header which is sent by the client. */
	static final String HEADER = "t";


	public GripperSimulReadInData() {
		super();
	}


	@Override
	public String toString() {
		return getClass().getSimpleName() + "[header=" + HEADER + "]";
	}
}
