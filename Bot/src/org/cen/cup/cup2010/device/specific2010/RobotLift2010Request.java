package org.cen.cup.cup2010.device.specific2010;

import org.cen.robot.device.RobotDeviceRequest;

public class RobotLift2010Request extends RobotDeviceRequest {
	public enum Action {
		DOWN, UP;
	}

	private Action action;

	public RobotLift2010Request(Action action) {
		super(Specific2010Device.NAME);
		this.action = action;
	}

	public Action getAction() {
		return action;
	}
}
