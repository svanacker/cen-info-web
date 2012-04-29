package org.cen.robot.device.battery;

import org.cen.robot.device.RobotDeviceRequest;
import org.cen.robot.device.RobotDeviceResult;

public class BatteryReadResult extends RobotDeviceResult {
	private double voltage;

	public BatteryReadResult(RobotDeviceRequest request) {
		super(request);
	}

	public double getVoltage() {
		return voltage;
	}
}
