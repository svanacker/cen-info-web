package org.cen.ui.web;

import java.util.logging.Logger;

import org.cen.com.IComService;
import org.cen.logging.LoggingUtils;
import org.cen.robot.device.servo.ServoChangeListener;
import org.cen.robot.device.servo.ServoData;
import org.cen.robot.device.servo.com.ServoOutData;
import org.cen.robot.services.IRobotService;
import org.cen.robot.services.IRobotServiceProvider;

/**
 * Presentation Objet for the view Servo.
 */
public class ServoView implements IRobotService, ServoChangeListener {

	private final static Logger logger = LoggingUtils.getClassLogger();

	private int count = 1;

	private ServoData[] data;

	private int maxCount;

	private IRobotServiceProvider servicesProvider;

	public ServoView() {
		super();
		setMaxCount(6);
	}

	public int getCount() {
		return count;
	}

	public ServoData[] getData() {
		return data;
	}

	public int getMaxCount() {
		return maxCount;
	}

	@Override
	public void onServoChange(ServoData servoData) {
		logger.finest("servo change: " + servoData.toString());
		sendData(servoData);
	}

	private void sendData(ServoData servoData) {
		IComService comService = servicesProvider.getService(IComService.class);
		comService.writeOutData(new ServoOutData(servoData));
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setData(ServoData[] data) {
		this.data = data;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
		if (data == null) {
			data = new ServoData[maxCount];
		} else if (data.length < maxCount) {
			ServoData[] oldData = data;
			data = new ServoData[maxCount];
			for (int i = 0; i < oldData.length; i++) {
				data[i] = oldData[i];
			}
		}
		for (int i = 0; i < data.length; i++) {
			if (data[i] == null) {
				data[i] = new ServoData(i);
				data[i].addServoChangeListener(this);
			}
		}
	}

	@Override
	public void setServicesProvider(IRobotServiceProvider provider) {
		servicesProvider = provider;
	}
}
