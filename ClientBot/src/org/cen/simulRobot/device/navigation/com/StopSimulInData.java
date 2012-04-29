package org.cen.simulRobot.device.navigation.com;

import org.cen.com.in.InData;


/**
 * Data which comes from the client and contains the stopMatch message.
 */
public class StopSimulInData extends InData {
	/** The message header which is sent by the client. */
	static final String HEADER = "Z";


	public StopSimulInData() {
		super();
	}


	@Override
	public String toString() {
		return getClass().getSimpleName() + "[header=" + HEADER + "]";
	}
}
