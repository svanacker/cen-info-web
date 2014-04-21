package org.cen.cup.cup2012.device.configuration2012.console;

import org.cen.robot.device.AbstractRobotDeviceAction;
import org.cen.robot.device.IRobotDevice;
import org.cen.robot.device.configuration.console.ConfigurationDeviceConsole;

/**
 * Specific configuration device console for the cup 2012.
 */
public class Configuration2012DeviceConsole extends ConfigurationDeviceConsole {

	public Configuration2012DeviceConsole(IRobotDevice device) {
		super(device);

		addAction(new AbstractRobotDeviceAction("flagViolet", "2. flagViolet") {
			private static final long serialVersionUID = 1L;

			@Override
			public void execute(Object parameters) {
				Configuration2012DeviceConsole.this.device.debug("flagViolet");
			}
		});

		addAction(new AbstractRobotDeviceAction("flagRed", "2. flagRed") {
			private static final long serialVersionUID = 1L;

			@Override
			public void execute(Object parameters) {
				Configuration2012DeviceConsole.this.device.debug("flagRed");
			}
		});

		addAction(new AbstractRobotDeviceAction("done", "3. done") {
			private static final long serialVersionUID = 1L;

			@Override
			public void execute(Object parameters) {
				Configuration2012DeviceConsole.this.device.debug("done");
			}
		});
	}
}
