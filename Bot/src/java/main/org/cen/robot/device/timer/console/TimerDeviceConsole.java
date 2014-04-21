package org.cen.robot.device.timer.console;

import java.util.Properties;

import org.cen.robot.device.AbstractRobotDeviceAction;
import org.cen.robot.device.IRobotDevice;
import org.cen.robot.device.console.AbstractRobotDeviceConsole;
import org.cen.robot.device.timer.TimerDevice;

/**
 * Console of the timer device.
 * 
 * @author Emmanuel ZURMELY
 */
public class TimerDeviceConsole extends AbstractRobotDeviceConsole {
	private static final long serialVersionUID = 1L;

	private TimerDevice device;

	/**
	 * Constructor.
	 * 
	 * @param device
	 *            the timer device
	 */
	public TimerDeviceConsole(IRobotDevice device) {
		super();
		this.device = (TimerDevice) device;
		properties = new Properties();
		properties.setProperty("duration", "90");

		addAction(new AbstractRobotDeviceAction("initialized", "1. robot initialized") {
			private static final long serialVersionUID = 1L;

			@Override
			public void execute(Object parameters) {
				TimerDeviceConsole.this.device.debug("initialized");
			}
		});
		addAction(new AbstractRobotDeviceAction("start", "start timer") {
			private static final long serialVersionUID = 1L;

			@Override
			public void execute(Object parameters) {
				TimerDeviceConsole.this.device.debug("start");
			}
		});
	}

	@Override
	public String getName() {
		return device.getName();
	}
}
