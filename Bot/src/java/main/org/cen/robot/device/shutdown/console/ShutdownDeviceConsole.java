package org.cen.robot.device.shutdown.console;

import org.cen.robot.device.IRobotDevice;
import org.cen.robot.device.console.AbstractRobotDeviceConsole;
import org.cen.robot.device.shutdown.ShutdownDevice;

/**
 * Console for handling the computer shut down.
 */
public class ShutdownDeviceConsole extends AbstractRobotDeviceConsole {
	private static final long serialVersionUID = 1L;

	private ShutdownDevice device;

	/**
	 * Constructor.
	 * 
	 * @param device
	 *            the shutdown device
	 */
	public ShutdownDeviceConsole(IRobotDevice device) {
		super();
		this.device = (ShutdownDevice) device;
	}

	@Override
	public String getName() {
		return device.getName();
	}
}
