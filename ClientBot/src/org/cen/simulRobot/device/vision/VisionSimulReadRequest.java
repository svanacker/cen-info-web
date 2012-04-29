package org.cen.simulRobot.device.vision;

import org.cen.robot.device.RobotDeviceRequest;

/**
 * Request for sending absolute position of an element.
 * 
 * @author Omar Benouamer
 */
public abstract class VisionSimulReadRequest extends RobotDeviceRequest {

	public VisionSimulReadRequest(String deviceName) {
		super(deviceName);
	}
}
