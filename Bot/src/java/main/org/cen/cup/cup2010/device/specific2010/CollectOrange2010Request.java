package org.cen.cup.cup2010.device.specific2010;

import org.cen.robot.device.request.impl.RobotDeviceRequest;

public class CollectOrange2010Request extends RobotDeviceRequest {
	public enum Action {
		PICK, RELEASE;
	}

	private Action action;

	public CollectOrange2010Request(Action action) {
		super(Specific2010Device.NAME);
		this.action = action;
	}

	public Action getAction() {
		return action;
	}
}
