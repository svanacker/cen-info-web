package org.cen.robot.device.battery.com;

import org.cen.com.in.InData;

/**
 * Encapsulation of the message which gives the level of the battery.
 * 
 * @author svanacker
 */
public class BatteryReadInData extends InData {
	public static final String HEADER = "b";

	private final int voltage;

	/**
	 * Constructor
	 * 
	 * @param voltage
	 *            voltage in mV
	 */
	public BatteryReadInData(int voltage) {
		super();
		this.voltage = voltage;
	}

	/**
	 * Returns the battery voltage in mV.
	 * 
	 * @return the battery voltage
	 */
	public int getVoltage() {
		return voltage;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[voltage=" + voltage + "]";
	}
}
