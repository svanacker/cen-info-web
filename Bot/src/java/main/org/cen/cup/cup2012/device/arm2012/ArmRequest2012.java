package org.cen.cup.cup2012.device.arm2012;

import org.cen.robot.device.RobotDeviceRequest;

public class ArmRequest2012 extends RobotDeviceRequest {

	private final ArmType2012 type;

	public ArmType2012 getType() {
		return type;
	}

	public ArmRequest2012(ArmType2012 type) {
		super(Arm2012Device.NAME);
		this.type = type;
	}
}