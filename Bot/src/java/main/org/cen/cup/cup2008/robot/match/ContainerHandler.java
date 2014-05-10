package org.cen.cup.cup2008.robot.match;

import org.cen.cup.cup2008.device.container.ContainerDevice;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.IRobotDeviceListener;
import org.cen.robot.device.RobotDeviceResult;
import org.cen.robot.match.IMatchStrategy;
import org.cen.robot.services.IRobotServiceProvider;

public class ContainerHandler implements IRobotDeviceListener {
	private IRobotServiceProvider servicesProvider;

	public ContainerHandler(IRobotServiceProvider servicesProvider) {
		super();
		this.servicesProvider = servicesProvider;
		IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
		handler.addDeviceListener(this);
	}

	@Override
	public String getDeviceName() {
		return ContainerDevice.NAME;
	}

	@Override
	public void handleResult(RobotDeviceResult result) {
		IMatchStrategy strategy = servicesProvider.getService(IMatchStrategy.class);
		strategy.notifyEvent(new ObjectCollectedEvent());
	}
}
