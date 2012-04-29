package org.cen.simulRobot.device.lcd.com;

import org.cen.com.in.InData;

public class LCDSimulInData extends InData {
	public static final String HEADER = "L";

	String text;

	/**
	 * Constructor with all arguments.
	 * 
	 * @param text
	 * 
	 */

	public LCDSimulInData(String atexte) {
		super();
		this.text = atexte;
	}

	public String getText() {
		return text;
	}



	@Override
	public String toString() {
		return getClass().getSimpleName() + "[text=" + text +"]";
	}
}
