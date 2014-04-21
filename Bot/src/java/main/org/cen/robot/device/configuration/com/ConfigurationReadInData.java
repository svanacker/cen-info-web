package org.cen.robot.device.configuration.com;

import org.cen.com.in.InData;

/**
 * Data which comes from the client and contains the configuration.
 */
public class ConfigurationReadInData extends InData {
	/** The message header which is sent by the client. */  
	static final String HEADER = "c";

	/** The value of configuration (bitmask). */
	private int value;

	public ConfigurationReadInData(int value) {
		super();
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[value=" + Integer.toHexString(value) + "]";
	}
}
