package org.cen.robot.device.timer;

import org.cen.robot.device.RobotDeviceRequest;

public class MatchFinishedRequest extends RobotDeviceRequest {
	public MatchFinishedRequest() {
		super(TimerDevice.NAME);
		// higher priority
		priority = 10;
	}
}
