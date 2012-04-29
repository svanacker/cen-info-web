package org.cen.simulRobot.device.navigation.console;

import java.util.Properties;

import org.cen.robot.device.IRobotDevice;
import org.cen.robot.device.console.AbstractRobotDeviceConsole;
import org.cen.simulRobot.device.navigation.NavigationSimulDevice;

public class NavigationSimulDeviceConsole extends AbstractRobotDeviceConsole{

	private NavigationSimulDevice device;

	/**
	 * Constructor.
	 * 
	 * @param device
	 *            the timer device
	 */
	public NavigationSimulDeviceConsole(IRobotDevice device) {
		super();
		this.device = (NavigationSimulDevice) device;
		properties = new Properties();
	}

	@Override
	public String getName() {
		return device.getName();
	}

}
