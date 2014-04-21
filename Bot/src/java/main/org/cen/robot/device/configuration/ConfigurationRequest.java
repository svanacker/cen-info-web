package org.cen.robot.device.configuration;

import org.cen.robot.device.RobotDeviceRequest;

public abstract class ConfigurationRequest extends RobotDeviceRequest {
	public ConfigurationRequest() {
		super(ConfigurationDevice.NAME);
	}
}
