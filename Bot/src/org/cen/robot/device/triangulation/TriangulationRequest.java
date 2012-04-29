package org.cen.robot.device.triangulation;

import org.cen.robot.device.RobotDeviceRequest;

/**
 * Base class of request objects sent to to the triangulation device.
 * 
 * @author Emmanuel ZURMELY
 */
public abstract class TriangulationRequest extends RobotDeviceRequest {
	public TriangulationRequest() {
		super(TriangulationDevice.NAME);
	}
}
