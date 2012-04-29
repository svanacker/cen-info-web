package org.cen.simulRobot.device.timer;

public final class RobotInitializedSimulRequest extends TimerSimulReadRequest {

	public RobotInitializedSimulRequest() {
		super(TimerSimulDevice.NAME);
	}
}