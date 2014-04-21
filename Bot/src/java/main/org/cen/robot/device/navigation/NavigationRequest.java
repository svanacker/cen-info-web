package org.cen.robot.device.navigation;

import org.cen.robot.device.RobotDeviceRequest;

public abstract class NavigationRequest extends RobotDeviceRequest {

	public NavigationRequest() {
		super(NavigationDevice.NAME);
	}
}
