package org.cen.robot.device.collision;

import org.cen.robot.device.RobotDeviceRequest;

public class CollisionReadRequest extends RobotDeviceRequest {

	public CollisionReadRequest() {
		super(CollisionDetectionDevice.NAME);
	}
}
