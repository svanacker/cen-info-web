package org.cen.simulRobot.device.beacon.console;

import java.util.Properties;

import org.cen.robot.device.IRobotDevice;
import org.cen.robot.device.console.AbstractRobotDeviceConsole;
import org.cen.simulRobot.device.beacon.BeaconSimulDevice;

public class BeaconSimulDeviceConsole extends AbstractRobotDeviceConsole{

	private BeaconSimulDevice device;

	/**
	 * Constructor.
	 * 
	 * @param device
	 *            the timer device
	 */
	public BeaconSimulDeviceConsole(IRobotDevice device) {
		super();
		this.device = (BeaconSimulDevice) device;
		properties = new Properties();
	}

	@Override
	public String getName() {
		return device.getName();
	}

}
