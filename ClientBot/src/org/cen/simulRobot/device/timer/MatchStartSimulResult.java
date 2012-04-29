package org.cen.simulRobot.device.timer;

import org.cen.robot.device.RobotDeviceRequest;
import org.cen.robot.device.timer.TimerResult;

public class MatchStartSimulResult extends TimerResult {

	public MatchStartSimulResult(RobotDeviceRequest request) {
		super(request);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[...]";
	}
}
