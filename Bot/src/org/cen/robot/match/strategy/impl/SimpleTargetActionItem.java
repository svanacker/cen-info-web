package org.cen.robot.match.strategy.impl;

import org.cen.robot.device.RobotDeviceRequest;
import org.cen.robot.match.strategy.ITargetActionItem;

public class SimpleTargetActionItem implements ITargetActionItem {
	private RobotDeviceRequest request;

	public SimpleTargetActionItem(RobotDeviceRequest request) {
		super();
		this.request = request;
	}

	@Override
	public RobotDeviceRequest getRequest() {
		return request;
	}
}
