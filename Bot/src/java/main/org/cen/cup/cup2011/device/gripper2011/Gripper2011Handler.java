package org.cen.cup.cup2011.device.gripper2011;

import org.cen.robot.brain.AbstractDeviceHandler;
import org.cen.robot.device.RobotDeviceResult;
import org.cen.robot.match.IMatchStrategy;
import org.cen.robot.services.IRobotServiceProvider;

public class Gripper2011Handler extends AbstractDeviceHandler {

	public Gripper2011Handler(IRobotServiceProvider servicesProvider) {
		super(servicesProvider);
	}

	@Override
	public String getDeviceName() {
		return Gripper2011Device.NAME;
	}

	@Override
	public void handleResult(RobotDeviceResult result) {
		if (result instanceof GetKingPresenceResult2011) {
			GetKingPresenceResult2011 r = (GetKingPresenceResult2011) result;
			IMatchStrategy strategy = servicesProvider.getService(IMatchStrategy.class);
			strategy.notifyEvent(new KingPresenceEvent(r.isPawnPresent()));
		}
	}
}
