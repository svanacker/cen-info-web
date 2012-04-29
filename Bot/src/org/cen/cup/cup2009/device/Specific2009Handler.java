package org.cen.cup.cup2009.device;

import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.RobotDeviceListener;
import org.cen.robot.device.RobotDeviceResult;

public class Specific2009Handler implements RobotDeviceListener {
	private IRobotServiceProvider servicesProvider;

	public Specific2009Handler(IRobotServiceProvider servicesProvider) {
		super();
		this.servicesProvider = servicesProvider;
		IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
		handler.addDeviceListener(this);
	}

	@Override
	public String getDeviceName() {
		return Specific2009Device.NAME;
	}

	@Override
	public void handleResult(RobotDeviceResult result) {
	}
}
