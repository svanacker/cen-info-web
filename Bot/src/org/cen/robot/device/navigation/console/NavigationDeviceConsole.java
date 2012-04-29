package org.cen.robot.device.navigation.console;

import org.cen.com.IComService;
import org.cen.robot.device.AbstractRobotDeviceAction;
import org.cen.robot.device.IRobotDevice;
import org.cen.robot.device.console.AbstractRobotDeviceConsole;
import org.cen.robot.device.navigation.NavigationDevice;
import org.cen.robot.device.navigation.com.StopOutData;
import org.cen.robot.device.navigation.position.com.ReadPositionPulseOutData;

public class NavigationDeviceConsole extends AbstractRobotDeviceConsole {
	private class DebugAction extends AbstractRobotDeviceAction {
		private static final long serialVersionUID = 1L;

		public DebugAction(String name, String label) {
			super(name, label);
		}

		@Override
		public void execute(Object parameters) {
			device.debug(getName());
		}
	}

	private final NavigationDevice device;

	public NavigationDeviceConsole(IRobotDevice device) {
		super();
		this.device = (NavigationDevice) device;

		final IComService comService = this.device.getServicesProvider().getService(IComService.class);

		addAction(new DebugAction("reached", "reached"));
		addAction(new DebugAction("positionRead", "read position"));
		addAction(new AbstractRobotDeviceAction("stop", "stop") {
			private static final long serialVersionUID = 1L;

			public void execute(Object parameters) {
				comService.writeOutData(new StopOutData());
			}
		});
		addAction(new AbstractRobotDeviceAction("position", "position") {
			private static final long serialVersionUID = 1L;

			public void execute(Object parameters) {
				comService.writeOutData(new ReadPositionPulseOutData());
			}
		});
	}

	@Override
	public String getName() {
		return device.getName();
	}
}
