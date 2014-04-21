package org.cen.robot.device.lcd.com;

import org.cen.com.ComDataUtils;
import org.cen.com.documentation.DeviceDataSignature;
import org.cen.com.documentation.DeviceMethodSignature;
import org.cen.com.documentation.DeviceMethodType;
import org.cen.com.documentation.DeviceParameter;
import org.cen.com.documentation.DeviceParameterType;
import org.cen.com.out.OutData;
import org.cen.robot.device.lcd.LcdDevice;

/**
 * OutData to print Text to the LCD.
 * 
 * @author svanacker
 * @version 24/02/2008
 */
@DeviceDataSignature(deviceName = LcdDevice.NAME, methods = { @DeviceMethodSignature(
		header = LcdPrintOutData.HEADER,
		type = DeviceMethodType.INPUT,
		parameters = { @DeviceParameter(name = "length", length = 2, type = DeviceParameterType.UNSIGNED, unit = ""),
				@DeviceParameter(name = "char1", length = 2, type = DeviceParameterType.UNSIGNED, unit = ""),
				@DeviceParameter(name = "char2", length = 2, type = DeviceParameterType.UNSIGNED, unit = ""),
				@DeviceParameter(name = "char3", length = 2, type = DeviceParameterType.UNSIGNED, unit = ""),
				@DeviceParameter(name = "char4", length = 2, type = DeviceParameterType.UNSIGNED, unit = ""), }) })
public class LcdPrintOutData extends OutData {

	public static final char CLS = 12;

	protected static final String HEADER = "L";

	/**
	 * The maximal size of written text.
	 */
	private static final int LCD_MAX_SIZE = 4;

	/** Text which is written */
	private final String text;

	/**
	 * Constructor
	 * 
	 * @param text
	 *            the text which must be sent to the lcd.
	 */
	public LcdPrintOutData(String text) {
		super();
		if (text.length() > LCD_MAX_SIZE) {
			throw new IllegalArgumentException("text must not have a length > " + LCD_MAX_SIZE + " : " + text);
		}
		this.text = text;
	}

	@Override
	public String getArguments() {
		String result = "";
		// We give the length of the String
		int textLength = text.length();
		result += ComDataUtils.format(textLength, 2);
		// We pass each character as int value
		for (int i = 0; i < textLength; i++) {
			char c = text.charAt(i);
			String s = ComDataUtils.format(c, 2);
			result += s;
		}
		for (int i = textLength; i < LCD_MAX_SIZE; i++) {
			result += "00";
		}

		return result;
	}

	public String getText() {
		return text;
	}

	@Override
	public String getHeader() {
		return HEADER;
	}

	@Override
	public String toString() {
		if (text == null) {
			return getClass().getSimpleName() + "[text:null]";
		} else {
			return getClass().getSimpleName() + "[length=" + text.length() + ", text=" + getText() + "]";
		}
	}
}
