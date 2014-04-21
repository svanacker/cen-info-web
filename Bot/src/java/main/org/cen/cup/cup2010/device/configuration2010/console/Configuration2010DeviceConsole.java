package org.cen.cup.cup2010.device.configuration2010.console;

import org.cen.robot.device.AbstractRobotDeviceAction;
import org.cen.robot.device.IRobotDevice;
import org.cen.robot.device.configuration.console.ConfigurationDeviceConsole;

/**
 * Specific configuration device console for the cup 2010.
 * 
 * @author Emmanuel ZURMELY
 */
public class Configuration2010DeviceConsole extends ConfigurationDeviceConsole {
	public Configuration2010DeviceConsole(IRobotDevice device) {
		super(device);

		addAction(new AbstractRobotDeviceAction("flagBlue", "2. flagBlue") {
			private static final long serialVersionUID = 1L;

			@Override
			public void execute(Object parameters) {
				Configuration2010DeviceConsole.this.device.debug("flagBlue");
			}
		});

		addAction(new AbstractRobotDeviceAction("flagYellow", "2. flagYellow") {
			private static final long serialVersionUID = 1L;

			@Override
			public void execute(Object parameters) {
				Configuration2010DeviceConsole.this.device.debug("flagYellow");
			}
		});

		addAction(new AbstractRobotDeviceAction("done", "3. done") {
			private static final long serialVersionUID = 1L;

			@Override
			public void execute(Object parameters) {
				Configuration2010DeviceConsole.this.device.debug("done");
			}
		});
	}
}
