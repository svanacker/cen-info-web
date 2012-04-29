package org.cen.cup.cup2011.device.configuration2011;

import org.cen.cup.cup2011.robot.match.ConfigurationHandler2011;
import org.cen.robot.device.configuration.ConfigurationDevice;
import org.cen.robot.device.configuration.ConfigurationReadResult;

/**
 * Specific configuration device for the cup 2011.
 * 
 * @author Emmanuel ZURMELY
 */
public class Configuration2011Device extends ConfigurationDevice {
	int value = 0;

	@Override
	public void debug(String debugAction) {
		super.debug(debugAction);
		if (debugAction.equals("flagBlue")) {
			// set bit
			value |= (1 << ConfigurationHandler2011.SWITCH_MATCHSIDE);
		} else if (debugAction.equals("flagRed")) {
			// clear bit
			value &= ~(1 << ConfigurationHandler2011.SWITCH_MATCHSIDE);
		} else if (debugAction.equals("bonus")) {
			// clear bit
			value ^= 1 << ConfigurationHandler2011.SWITCH_BONUS;
		} else if (debugAction.equals("build")) {
			// clear bit
			value ^= 1 << ConfigurationHandler2011.SWITCH_BUILD;
		} else if (debugAction.equals("done")) {
			notifyResult(new ConfigurationReadResult(null, value));
		}
	}
}
