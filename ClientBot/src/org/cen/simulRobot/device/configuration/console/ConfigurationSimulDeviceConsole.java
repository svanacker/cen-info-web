package org.cen.simulRobot.device.configuration.console;

import java.util.Properties;

import org.cen.robot.device.IRobotDevice;
import org.cen.robot.device.console.AbstractRobotDeviceConsole;
import org.cen.simulRobot.device.configuration.ConfigurationSimulDevice;

public class ConfigurationSimulDeviceConsole extends AbstractRobotDeviceConsole{

	private ConfigurationSimulDevice device;

	/**
	 * Constructor.
	 * 
	 * @param device
	 *            the timer device
	 */
	public ConfigurationSimulDeviceConsole(IRobotDevice device) {
		super();
		this.device = (ConfigurationSimulDevice) device;
		properties = new Properties();
	}

	@Override
	public String getName() {
		return device.getName();
	}

}
