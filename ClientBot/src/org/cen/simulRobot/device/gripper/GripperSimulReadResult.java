package org.cen.simulRobot.device.gripper;

import org.cen.robot.device.RobotDeviceRequest;
import org.cen.robot.device.RobotDeviceResult;

public class GripperSimulReadResult extends RobotDeviceResult {
	private String value;

	public GripperSimulReadResult(RobotDeviceRequest request) {
		super(request);
		this.value = "t";
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[value: " + value + "]";
	}
}
