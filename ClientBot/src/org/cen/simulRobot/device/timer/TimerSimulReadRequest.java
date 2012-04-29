package org.cen.simulRobot.device.timer;

import org.cen.robot.device.RobotDeviceRequest;

/**
 * Request for reading the timer.
 * 
 */
public abstract class TimerSimulReadRequest extends RobotDeviceRequest {

	public TimerSimulReadRequest(String deviceName) {
		super(deviceName);
	}
}