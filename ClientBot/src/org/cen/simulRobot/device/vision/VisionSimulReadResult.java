package org.cen.simulRobot.device.vision;

import org.cen.robot.device.RobotDeviceRequest;
import org.cen.robot.device.RobotDeviceResult;

public class VisionSimulReadResult extends RobotDeviceResult {
	private String value;

	public VisionSimulReadResult(RobotDeviceRequest request) {
		super(request);
		this.value = "Vision information request";
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[value: " + value + "]";
	}
}
