package org.cen.ui.web;

import java.util.logging.Logger;

import org.cen.com.IComService;
import org.cen.logging.LoggingUtils;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.lcd.LcdWriteRequest;
import org.cen.robot.device.lcd.com.LcdBacklightOutData;
import org.cen.robot.device.lcd.com.LcdPrintOutData;
import org.cen.robot.services.IRobotService;
import org.cen.robot.services.IRobotServiceProvider;

/**
 * Encapsulates the view for the LCD
 * 
 * @author svanacker
 */
public class LCDView implements IRobotService {

	private final static Logger logger = LoggingUtils.getClassLogger();

	private IRobotServiceProvider servicesProvider;

	private boolean backLight;

	protected String text;

	public LCDView() {
		super();
		backLight = true;
		text = "";
	}

	@Override
	public void setServicesProvider(IRobotServiceProvider provider) {
		servicesProvider = provider;
	}

	public boolean isBackLight() {
		return backLight;
	}

	public void setBackLight(boolean backLight) {
		if (this.backLight != backLight) {
			this.backLight = backLight;
			notifyBackLightChanged();
		}
		this.backLight = backLight;
	}

	protected void notifyBackLightChanged() {
		logger.finest("lcd backLight change: " + backLight);
		sendBackLightData();
	}

	protected void sendBackLightData() {
		IComService comService = servicesProvider.getService(IComService.class);
		comService.writeOutData(new LcdBacklightOutData(backLight));
	}

	public void clearScreen() {
		String clsInstruction = String.valueOf(LcdPrintOutData.CLS);
		sendTextData(clsInstruction);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void sendText() {
		if (text != null && text.length() > 0) {
			logger.finest("LCD Send Text: " + text);
			sendTextData(text);
			setText("");
		}
	}

	protected void notifyTextChanged(String aText) {
		logger.finest("LCD Text: " + aText);
		sendTextData(aText);
		setText("");
	}

	protected void sendTextData(String aText) {
		IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
		LcdWriteRequest request = new LcdWriteRequest(aText);
		handler.sendRequest(request);

		// IComService comService =
		// servicesProvider.getService(IComService.class);
		// comService.writeOutData(new LcdPrintOutData(aText));
	}

}
