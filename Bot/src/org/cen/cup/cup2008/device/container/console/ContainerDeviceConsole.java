package org.cen.cup.cup2008.device.container.console;

import org.cen.cup.cup2008.device.container.ContainerDevice;
import org.cen.robot.device.AbstractRobotDeviceAction;
import org.cen.robot.device.IRobotDevice;
import org.cen.robot.device.console.AbstractRobotDeviceConsole;

public class ContainerDeviceConsole extends AbstractRobotDeviceConsole {
	private static final long serialVersionUID = 1L;

	private ContainerDevice device;

	public ContainerDeviceConsole(IRobotDevice device) {
		super();
		this.device = (ContainerDevice) device;

		addAction(new AbstractRobotDeviceAction("open", "open") {
			private static final long serialVersionUID = 1L;

			@Override
			public void execute(Object parameters) {
				ContainerDeviceConsole.this.device.debug("open");
			}
		});

		addAction(new AbstractRobotDeviceAction("close", "close") {
			private static final long serialVersionUID = 1L;

			@Override
			public void execute(Object parameters) {
				ContainerDeviceConsole.this.device.debug("close");
			}
		});

		addAction(new AbstractRobotDeviceAction("move", "move") {
			private static final long serialVersionUID = 1L;

			@Override
			public void execute(Object parameters) {
				ContainerDeviceConsole.this.device.debug("move");
			}
		});
	}

	@Override
	public String getName() {
		return device.getName();
	}
}
