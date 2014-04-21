package org.cen.cup.cup2010.device.specific2010;

import java.util.EnumSet;

import org.cen.robot.device.RobotDeviceRequest;

public class ReleaseObjects2010Request extends RobotDeviceRequest {
	public enum Target {
		ORANGE, TOMATO, CORN;
	}

	private EnumSet<Target> target;

	public ReleaseObjects2010Request(EnumSet<Target> target) {
		super(Specific2010Device.NAME);
		this.target = target;
	}

	public EnumSet<Target> getTarget() {
		return target;
	}
}
