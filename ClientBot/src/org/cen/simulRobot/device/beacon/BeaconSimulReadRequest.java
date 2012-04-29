package org.cen.simulRobot.device.beacon;

import org.cen.robot.device.RobotDeviceRequest;

/**
 * Request for reading the configuration.
 * 
 * @author Omar BENOUAMER
 */
public final class BeaconSimulReadRequest extends RobotDeviceRequest {

	protected int x;
	protected int y;

	/**
	 * Constructeur
	 * 
	 * @param ax
	 * @param ay
	 */
	public BeaconSimulReadRequest(int ax, int ay) {
		super(BeaconSimulDevice.NAME);
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
