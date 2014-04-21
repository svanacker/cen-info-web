package org.cen.robot.device.lcd.com;

import org.cen.com.ComDataUtils;
import org.cen.com.out.OutData;

/**
 * OutData to enable or disable the backlight of LCD.
 * @author svanacker
 */
public class LcdBacklightOutData extends OutData {
	private static final String HEADER = "K";

	/** If the ldc must have backLight or not. */
	private boolean backLight;

	/**
	 * Constructor
	 * @param lcdData encapsulation of data of Lcd.
	 */
	public LcdBacklightOutData(boolean backLight) {
		super();
		this.backLight = backLight;
	}

	@Override
	public String getArguments() {
		String backLightValue = ComDataUtils.format(backLight);
		return backLightValue;
	}

	@Override
	public String getHeader() {
		return HEADER;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[backLight=" + backLight + "]";
	}
}
