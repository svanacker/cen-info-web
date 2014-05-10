package org.cen.cup.cup2010.robot.match;

import org.cen.cup.cup2010.device.specific2010.CollectDoneResult;
import org.cen.cup.cup2010.device.specific2010.Specific2010Device;
import org.cen.robot.brain.AbstractDeviceHandler;
import org.cen.robot.device.RobotDeviceResult;
import org.cen.robot.device.timer.MatchStartedResult;
import org.cen.robot.match.IMatchStrategy;
import org.cen.robot.services.IRobotServiceProvider;

public class Specific2010Handler extends AbstractDeviceHandler {
	public Specific2010Handler(IRobotServiceProvider servicesProvider) {
		super(servicesProvider);
	}

	@Override
	public String getDeviceName() {
		return Specific2010Device.NAME;
	}

	@Override
	public void handleResult(RobotDeviceResult result) {
		IMatchStrategy strategy = servicesProvider.getService(IMatchStrategy.class);
		if (result instanceof MatchStartedResult) {
			strategy.notifyEvent(new CornFixedEvent());
		} else if (result instanceof CollectDoneResult) {
			strategy.notifyEvent(new CollectDoneEvent());
		}
	}
}
