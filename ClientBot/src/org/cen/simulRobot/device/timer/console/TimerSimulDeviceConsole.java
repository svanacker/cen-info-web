package org.cen.simulRobot.device.timer.console;

import java.util.Properties;

import org.cen.robot.device.AbstractRobotDeviceAction;
import org.cen.robot.device.IRobotDevice;
import org.cen.robot.device.console.AbstractRobotDeviceConsole;
import org.cen.simulRobot.device.timer.TimerSimulDevice;

public class TimerSimulDeviceConsole extends AbstractRobotDeviceConsole{

	private TimerSimulDevice device;

	/**
	 * Constructor.
	 * 
	 * @param device
	 *            the timer device
	 */
	public TimerSimulDeviceConsole(IRobotDevice device) {
		super();
		this.device = (TimerSimulDevice) device;
		properties = new Properties();
		properties.setProperty("Duration", "90 secondes");

		addAction(new AbstractRobotDeviceAction("initialized", "1. robot initialized") {
			private static final long serialVersionUID = 1L;

			@Override
			public void execute(Object parameters) {
				TimerSimulDeviceConsole.this.device.debug("initialized");
			}
		});
		addAction(new AbstractRobotDeviceAction("start", "start timer") {
			private static final long serialVersionUID = 1L;

			@Override
			public void execute(Object parameters) {
				TimerSimulDeviceConsole.this.device.debug("start");
			}
		});
	}

	@Override
	public String getName() {
		return device.getName();
	}

}
