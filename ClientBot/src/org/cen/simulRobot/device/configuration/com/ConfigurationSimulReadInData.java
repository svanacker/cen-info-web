package org.cen.simulRobot.device.configuration.com;

import org.cen.com.in.InData;

/**
 * Data which comes from the client and contains the configuration.
 */
public class ConfigurationSimulReadInData extends InData {
	/** The message header which is sent by the client. */
	static final String HEADER = "c";


	public ConfigurationSimulReadInData() {
		super();
	}


	@Override
	public String toString() {
		return getClass().getSimpleName() + "[header=" + HEADER + "]";
	}
}
