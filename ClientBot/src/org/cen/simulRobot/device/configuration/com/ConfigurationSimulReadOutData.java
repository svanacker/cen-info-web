package org.cen.simulRobot.device.configuration.com;

import org.cen.com.ComDataUtils;
import org.cen.com.out.OutData;

/**
 * Serial data for reading the configuration device.
 * 
 * @author Omar BENOUAMER
 */
public class ConfigurationSimulReadOutData extends OutData {
	static final String HEADER = "c";

	/** The value of configuration (bitmask). */
	private Integer value;

	public ConfigurationSimulReadOutData(int avalue){
		super();
		this.value = avalue;
	}

	@Override
	public String getArguments() {
		String result = "";

		String s = ComDataUtils.format(value, 2);
		result += s;

		return result;
	}

	@Override
	public String getHeader() {
		return HEADER;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[header=" + HEADER + "[value=" + value + "]";
	}
}
