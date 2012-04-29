package org.cen.simulRobot.device.configuration;

import org.cen.robot.device.configuration.ConfigurationRequest;

/**
 * Request for reading the configuration.
 * 
 * @author Omar Benouamer
 */
public final class ConfigurationSimulReadRequest extends ConfigurationRequest {
	protected int valeur;
	/**
	 * Constructor.
	 * @param ainterrupteurs
	 */
	public ConfigurationSimulReadRequest(int avaleur) {
		super();
		this.valeur =  avaleur;
	}
	public int getValeur() {
		return valeur;
	}

}
