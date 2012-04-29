package org.cen.cup.cup2010.device.specific2010;

import org.cen.robot.device.RobotDeviceRequest;

public class CollectCorn2010Request extends RobotDeviceRequest {
	public enum Action {
		DOWN, COLLECT, RELEASE, SEQUENCE;
	}

	public enum Side {
		LEFT, RIGHT;
	}

	private Action action;

	private Side side;

	public CollectCorn2010Request(Side side, Action action) {
		super(Specific2010Device.NAME);
		this.side = side;
		this.action = action;
	}

	public Action getAction() {
		return action;
	}

	public Side getSide() {
		return side;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[action=" + action + ", side=" + side + "]";
	}
}
