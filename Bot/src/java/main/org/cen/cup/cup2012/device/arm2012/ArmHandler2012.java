package org.cen.cup.cup2012.device.arm2012;

import org.cen.robot.brain.AbstractDeviceHandler;
import org.cen.robot.device.RobotDeviceResult;
import org.cen.robot.services.IRobotServiceProvider;

public class ArmHandler2012 extends AbstractDeviceHandler {

	public ArmHandler2012(IRobotServiceProvider servicesProvider) {
		super(servicesProvider);
	}

	@Override
	public String getDeviceName() {
		return Arm2012Device.NAME;
	}

	@Override
	public void handleResult(RobotDeviceResult result) {

	}
}
