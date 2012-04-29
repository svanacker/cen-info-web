package org.cen.cup.cup2012.device.configuration2012;

import org.cen.cup.cup2012.robot.match.ConfigurationHandler2012;
import org.cen.robot.device.configuration.ConfigurationDevice;
import org.cen.robot.device.configuration.ConfigurationReadResult;

/**
 * Specific configuration device for the cup 2012.
 */
public class Configuration2012Device extends ConfigurationDevice {
	int value = (1 << ConfigurationHandler2012.SWITCH_MATCHSIDE);

	@Override
	public void debug(String debugAction) {
		super.debug(debugAction);
		if (debugAction.equals("flagViolet")) {
			// set bit
			value |= (1 << ConfigurationHandler2012.SWITCH_MATCHSIDE);
		} else if (debugAction.equals("flagRed")) {
			// clear bit
			value &= ~(1 << ConfigurationHandler2012.SWITCH_MATCHSIDE);
		} else if (debugAction.equals("done")) {
			notifyResult(new ConfigurationReadResult(null, value));
		}
	}
}
