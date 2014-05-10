package org.cen.cup.cup2010.device.specific2010.console;

import org.cen.cup.cup2010.device.specific2010.Specific2010Device;
import org.cen.robot.device.AbstractRobotDeviceAction;
import org.cen.robot.device.IRobotDevice;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.RobotDeviceDebugEvent;
import org.cen.robot.device.IRobotDeviceDebugListener;
import org.cen.robot.device.console.AbstractRobotDeviceConsole;

public class Specific2010DeviceConsole extends AbstractRobotDeviceConsole implements IRobotDeviceDebugListener {
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

	private Specific2010Device device;

	public Specific2010DeviceConsole(IRobotDevice device) {
		super();
		this.device = (Specific2010Device) device;
		IRobotDevicesHandler handler = this.device.getServicesProvider().getService(IRobotDevicesHandler.class);
		handler.addDeviceDebugListener(this);

		addAction(new DebugAction("nextStep", "nextStep"));
		addAction(new DebugAction("stepByStepOn", "stepByStepOn"));
		addAction(new DebugAction("stepByStepOff", "stepByStepOff"));
		addAction(new DebugAction("resetPosition", "resetPosition"));
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public String getName() {
		return device.getName();
	}

	@Override
	public void debugEvent(RobotDeviceDebugEvent e) {

	}

	@Override
	public String getDeviceName() {
		return device.getName();
	}

}
