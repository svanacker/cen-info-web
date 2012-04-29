package org.cen.simulRobot.device.configuration;

import org.cen.robot.device.RobotDeviceRequest;
import org.cen.robot.device.configuration.ConfigurationResult;

public class ConfigurationSimulReadResult extends ConfigurationResult {
	private String value;

	public ConfigurationSimulReadResult(RobotDeviceRequest request) {
		super(request);
		this.value = "c";
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[value: " + value + "]";
	}
}
