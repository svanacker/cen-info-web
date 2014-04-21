package org.cen.robot.device.collision.console;

import org.cen.robot.device.IRobotDevice;
import org.cen.robot.device.collision.CollisionDetectionDevice;
import org.cen.robot.device.console.AbstractRobotDeviceConsole;

public class CollisionDetectionDeviceConsole extends AbstractRobotDeviceConsole {
	protected IRobotDevice device;

	public CollisionDetectionDeviceConsole(IRobotDevice device) {
		super();
		this.device = device;
	}

	@Override
	public String getName() {
		return CollisionDetectionDevice.NAME;
	}
}
