package org.cen.simulRobot.device.lcd.console;

import java.util.Properties;

import org.cen.robot.device.IRobotDevice;
import org.cen.robot.device.console.AbstractRobotDeviceConsole;
import org.cen.simulRobot.device.lcd.LCDSimulDevice;

public class LCDSimulDeviceConsole extends AbstractRobotDeviceConsole{

	private LCDSimulDevice device;

	/**
	 * Constructor.
	 * 
	 * @param device
	 *            the timer device
	 */
	public LCDSimulDeviceConsole(IRobotDevice device) {
		super();
		this.device = (LCDSimulDevice) device;
		properties = new Properties();
	}

	@Override
	public String getName() {
		return device.getName();
	}

}
