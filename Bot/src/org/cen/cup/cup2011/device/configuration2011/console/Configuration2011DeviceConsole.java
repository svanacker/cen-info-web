package org.cen.cup.cup2011.device.configuration2011.console;

import org.cen.robot.device.AbstractRobotDeviceAction;
import org.cen.robot.device.IRobotDevice;
import org.cen.robot.device.configuration.console.ConfigurationDeviceConsole;

/**
 * Specific configuration device console for the cup 2011.
 * 
 * @author Emmanuel ZURMELY
 */
public class Configuration2011DeviceConsole extends ConfigurationDeviceConsole {
	public Configuration2011DeviceConsole(IRobotDevice device) {
		super(device);

		addAction(new AbstractRobotDeviceAction("flagBlue", "2. flagBlue") {
			private static final long serialVersionUID = 1L;

			@Override
			public void execute(Object parameters) {
				Configuration2011DeviceConsole.this.device.debug("flagBlue");
			}
		});

		addAction(new AbstractRobotDeviceAction("flagRed", "2. flagRed") {
			private static final long serialVersionUID = 1L;

			@Override
			public void execute(Object parameters) {
				Configuration2011DeviceConsole.this.device.debug("flagRed");
			}
		});

		addAction(new AbstractRobotDeviceAction("done", "3. done") {
			private static final long serialVersionUID = 1L;

			@Override
			public void execute(Object parameters) {
				Configuration2011DeviceConsole.this.device.debug("done");
			}
		});

		addAction(new AbstractRobotDeviceAction("bonus", "1. bonus") {
			private static final long serialVersionUID = 1L;

			@Override
			public void execute(Object parameters) {
				Configuration2011DeviceConsole.this.device.debug("bonus");
			}
		});

		addAction(new AbstractRobotDeviceAction("build", "1. build") {
			private static final long serialVersionUID = 1L;

			@Override
			public void execute(Object parameters) {
				Configuration2011DeviceConsole.this.device.debug("build");
			}
		});
	}
}
