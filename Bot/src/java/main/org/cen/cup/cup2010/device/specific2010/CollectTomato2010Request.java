package org.cen.cup.cup2010.device.specific2010;

import org.cen.robot.device.request.impl.RobotDeviceRequest;

public class CollectTomato2010Request extends RobotDeviceRequest {
	public enum Action {
		ON, OFF;
	}

	private Action action;

	public CollectTomato2010Request(Action action) {
		super(Specific2010Device.NAME);
		this.action = action;
	}

	public Action getAction() {
		return action;
	}
}
