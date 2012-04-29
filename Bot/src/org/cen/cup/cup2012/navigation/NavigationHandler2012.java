package org.cen.cup.cup2012.navigation;

import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.brain.AbstractDeviceHandler;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.RobotDeviceResult;
import org.cen.robot.device.navigation.NavigationDevice;
import org.cen.robot.device.navigation.NavigationInitializeRequest;
import org.cen.robot.device.navigation.NavigationResult;
import org.cen.robot.match.IMatchStrategy;
import org.cen.robot.match.events.MoveStoppedEvent;
import org.cen.robot.match.events.PositionReachedEvent;

public class NavigationHandler2012 extends AbstractDeviceHandler {

	public NavigationHandler2012(IRobotServiceProvider servicesProvider) {
		super(servicesProvider);

		// Initialisation de la navigation
		IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
		NavigationDevice device = (NavigationDevice) handler.getDevice(NavigationDevice.NAME);
		// Mode asynchrone
		device.setAsynchronous(true);
		handler.sendRequest(new NavigationInitializeRequest());
	}

	@Override
	public String getDeviceName() {
		return NavigationDevice.NAME;
	}

	@Override
	public void handleResult(RobotDeviceResult result) {
		if (result instanceof NavigationResult) {
			NavigationResult r = (NavigationResult) result;
			IMatchStrategy strategy = servicesProvider.getService(IMatchStrategy.class);
			switch (r.getStatus()) {
			case REACHED:
				strategy.notifyEvent(new PositionReachedEvent());
				break;
			case STOPPED:
				strategy.notifyEvent(new MoveStoppedEvent());
				break;
			}
		}
	}

	@Override
	public void shutdown() {
		IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
		handler.getRequestDispatcher().purgeQueue();
		super.shutdown();
	}
}
