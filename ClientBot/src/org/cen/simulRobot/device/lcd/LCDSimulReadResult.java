package org.cen.simulRobot.device.lcd;

import org.cen.robot.device.RobotDeviceRequest;
import org.cen.robot.device.RobotDeviceResult;

public class LCDSimulReadResult extends RobotDeviceResult {
	private String value;

	public LCDSimulReadResult(RobotDeviceRequest request) {
		super(request);
		this.value = "L";
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[value: " + value + "]";
	}
}
