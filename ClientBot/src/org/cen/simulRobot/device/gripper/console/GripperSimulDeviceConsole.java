package org.cen.simulRobot.device.gripper.console;

import java.util.Properties;

import org.cen.robot.device.IRobotDevice;
import org.cen.robot.device.console.AbstractRobotDeviceConsole;
import org.cen.simulRobot.device.gripper.GripperSimulDevice;


public class GripperSimulDeviceConsole extends AbstractRobotDeviceConsole{

	private GripperSimulDevice device;

	/**
	 * Constructor.
	 * 
	 * @param device
	 *            the timer device
	 */
	public GripperSimulDeviceConsole(IRobotDevice device) {
		super();
		this.device = (GripperSimulDevice) device;
		properties = new Properties();
	}

	@Override
	public String getName() {
		return device.getName();
	}

}
