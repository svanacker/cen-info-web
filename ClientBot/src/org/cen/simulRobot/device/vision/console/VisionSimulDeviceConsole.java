package org.cen.simulRobot.device.vision.console;

import java.util.Properties;

import org.cen.robot.device.IRobotDevice;
import org.cen.robot.device.console.AbstractRobotDeviceConsole;
import org.cen.simulRobot.device.vision.VisionSimulDevice;

public class VisionSimulDeviceConsole extends AbstractRobotDeviceConsole{

	private VisionSimulDevice device;

	/**
	 * Constructor.
	 * 
	 * @param device
	 *            the timer device
	 */
	public VisionSimulDeviceConsole(IRobotDevice device) {
		super();
		this.device = (VisionSimulDevice) device;
		properties = new Properties();
	}

	@Override
	public String getName() {
		return device.getName();
	}

}
