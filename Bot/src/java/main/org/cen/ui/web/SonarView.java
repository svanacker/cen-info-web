package org.cen.ui.web;

import java.util.logging.Logger;

import org.cen.logging.LoggingUtils;
import org.cen.robot.IRobotService;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.device.sonar.SonarData;

/**
 * Presentation Objet for the view Servo.
 */
public class SonarView implements IRobotService {

	private final static Logger logger = LoggingUtils.getClassLogger();

	private int count = 1;

	private SonarData[] data;

	private int maxCount;

	private IRobotServiceProvider servicesProvider;

	public SonarView() {
		super();
		setMaxCount(2);
	}

	public int getCount() {
		return count;
	}

	public SonarData[] getData() {
		return data;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void notifyChanged(SonarData sonarData) {
		logger.finest("sonar change: " + sonarData.toString());
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setData(SonarData[] data) {
		this.data = data;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
		if (data == null) {
            data = new SonarData[maxCount];
        } else if (data.length < maxCount) {
			SonarData[] oldData = data;
			data = new SonarData[maxCount];
			for (int i = 0; i < oldData.length; i++) {
                data[i] = oldData[i];
            }
		}
		for (int i = 0; i < data.length; i++) {
            if (data[i] == null) {
                data[i] = new SonarData(i, this);
            }
        }
	}

	@Override
	public void setServicesProvider(IRobotServiceProvider provider) {
		servicesProvider = provider;
	}
}
