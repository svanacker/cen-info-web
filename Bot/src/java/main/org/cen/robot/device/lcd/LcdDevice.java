package org.cen.robot.device.lcd;

import java.util.ArrayList;
import java.util.List;

import org.cen.com.IComService;
import org.cen.com.out.OutData;
import org.cen.robot.device.AbstractRobotDevice;
import org.cen.robot.device.RobotDeviceRequest;
import org.cen.robot.device.lcd.com.LcdPrintOutData;

public class LcdDevice extends AbstractRobotDevice {

	public static final String NAME = "lcd";

	public static final int MAX_LCD_TEXT_SIZE = 4;

	/**
	 * Constructor.
	 */
	public LcdDevice() {
		super(NAME);
	}

	@Override
	public void debug(String debugAction) {
	}

	protected List<OutData> getOutData(LcdWriteRequest request) {
		List<OutData> list = new ArrayList<OutData>();
		String text = request.getText();
		int end = 0;
		for (int i = 0; i < text.length(); i = i + MAX_LCD_TEXT_SIZE) {
			end = Math.min(i + MAX_LCD_TEXT_SIZE, text.length());
			String subString = text.substring(i, end);
			/*
			 * int subStringLength; for (subStringLength = subString.length();
			 * subStringLength < MAX_LCD_TEXT_SIZE; subStringLength++) {
			 * subString = subString.concat("_"); }
			 */
			list.add(new LcdPrintOutData(subString));
		}
		return list;
	}

	@Override
	protected void internalHandleRequest(RobotDeviceRequest request) {
		if (request instanceof LcdWriteRequest) {
			sendData((LcdWriteRequest) request);
		}
	}

	private void sendData(LcdWriteRequest request) {
		IComService comService = servicesProvider.getService(IComService.class);
		for (OutData outData : getOutData(request)) {
			comService.writeOutData(outData);
		}
	}
}
