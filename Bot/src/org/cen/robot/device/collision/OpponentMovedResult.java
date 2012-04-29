package org.cen.robot.device.collision;

import org.cen.robot.device.RobotDeviceRequest;
import org.cen.robot.device.collision.com.OpponentPositionInData;

public class OpponentMovedResult extends CollisionResult {
	private OpponentPositionInData position;

	public OpponentMovedResult(RobotDeviceRequest request, OpponentPositionInData position) {
		super(request);
		this.position = position;
	}

	public OpponentPositionInData getData() {
		return position;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[position=" + position + "]";
	}
}
