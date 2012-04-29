package org.cen.simulRobot.device.beacon.com;

import org.cen.com.in.InData;


public class BeaconSimulReadInData extends InData {
	/** The message header which is sent by the client. */
	static final String HEADER = "";


	public BeaconSimulReadInData() {
		super();
	}


	@Override
	public String toString() {
		return getClass().getSimpleName() + "[header=" + HEADER + "]";
	}
}
