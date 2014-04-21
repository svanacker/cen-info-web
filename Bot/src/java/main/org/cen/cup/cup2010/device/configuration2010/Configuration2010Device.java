package org.cen.cup.cup2010.device.configuration2010;

import org.cen.cup.cup2010.robot.match.ConfigurationHandler2010;
import org.cen.robot.device.configuration.ConfigurationDevice;
import org.cen.robot.device.configuration.ConfigurationReadResult;

/**
 * Specific configuration device for the cup 2010.
 * 
 * @author Emmanuel ZURMELY
 */
public class Configuration2010Device extends ConfigurationDevice {
	int value = 0;

	@Override
	public void debug(String debugAction) {
		super.debug(debugAction);
		if (debugAction.equals("flagBlue")) {
			// set bit
			value |= (1 << ConfigurationHandler2010.SWITCH_MATCHSIDE);
		} else if (debugAction.equals("flagYellow")) {
			// clear bit
			value &= ~(1 << ConfigurationHandler2010.SWITCH_MATCHSIDE);
		} else if (debugAction.equals("done")) {
			notifyResult(new ConfigurationReadResult(null, value));
		}
	}
}
