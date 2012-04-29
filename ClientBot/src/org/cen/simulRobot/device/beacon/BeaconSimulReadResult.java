package org.cen.simulRobot.device.beacon;

import org.cen.robot.device.RobotDeviceRequest;
import org.cen.robot.device.RobotDeviceResult;

public class BeaconSimulReadResult extends RobotDeviceResult {
	private String value;

	public BeaconSimulReadResult(RobotDeviceRequest request) {
		super(request);
		this.value = "";
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[value: " + value + "]";
	}
}
