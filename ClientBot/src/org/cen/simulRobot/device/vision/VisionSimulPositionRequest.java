package org.cen.simulRobot.device.vision;

import org.cen.robot.device.RobotDeviceRequest;

/**
 * Request for sending absolute position of an element.
 * 
 * @author Omar Benouamer
 */
public final class VisionSimulPositionRequest extends RobotDeviceRequest {

	protected int x;
	protected int y;

	/**
	 * Constructeur
	 * 
	 * @param ax
	 * @param ay
	 */
	public VisionSimulPositionRequest(int ax, int ay) {
		super(VisionSimulDevice.NAME);
		this.x =  ax;
		this.y =  ay;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
