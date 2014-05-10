package org.cen.cup.cup2009.device.console;

import org.cen.cup.cup2009.device.Specific2009Device;
import org.cen.robot.device.IRobotDevice;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.RobotDeviceDebugEvent;
import org.cen.robot.device.IRobotDeviceDebugListener;
import org.cen.robot.device.console.AbstractRobotDeviceConsole;

public class Specific2009DeviceConsole extends AbstractRobotDeviceConsole implements IRobotDeviceDebugListener {
	
	private Specific2009Device device;

	public Specific2009DeviceConsole(IRobotDevice device) {
		super();
		this.device = (Specific2009Device) device;
		IRobotDevicesHandler handler = this.device.getServicesProvider().getService(IRobotDevicesHandler.class);
		handler.addDeviceDebugListener(this);
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
