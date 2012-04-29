package org.cen.simulRobot.device.lcd.com;

import org.cen.com.ComDataUtils;
import org.cen.com.out.OutData;

/**
 * Serial data for reading the configuration device.
 * 
 * @author Emmanuel ZURMELY
 */
public class LCDSimulReadOutData extends OutData {
	static final String HEADER = "L";

	/** The value of configuration (bitmask). */
	private Integer value;

	public LCDSimulReadOutData(){
		super();
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
