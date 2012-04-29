package org.cen.simulRobot.device.lcd;

import org.cen.robot.device.RobotDeviceRequest;

/**
 * Request for reading LCD message.
 * 
 * @author Omar Benouamer
 */
public final class LCDSimulReadRequest extends RobotDeviceRequest {
	protected int valeur;
	/**
	 * Constructor.
	 * @param ainterrupteurs
	 */
	public LCDSimulReadRequest(int avaleur) {
		super(LCDSimulDevice.NAME);
		this.valeur =  avaleur;
	}
	public int getValeur() {
		return valeur;
	}
}
