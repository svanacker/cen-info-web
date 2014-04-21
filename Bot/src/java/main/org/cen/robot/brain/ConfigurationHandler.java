package org.cen.robot.brain;

import java.util.logging.Logger;

import org.cen.logging.LoggingUtils;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.device.RobotDeviceResult;
import org.cen.robot.device.configuration.ConfigurationDevice;
import org.cen.robot.device.configuration.ConfigurationReadResult;
import org.cen.robot.match.IMatchStrategy;
import org.cen.robot.match.events.MatchConfigurationDone;

/**
 * Abstract base class of the configuration handler. The configuration handler
 * handles the configuration event notified by the configuration device by
 * updating the data and notifying the math strategy handler that the
 * configuration has changed.
 * 
 * @author Emmanuel ZURMELY
 * 
 */
public abstract class ConfigurationHandler extends AbstractDeviceHandler {

	private static final Logger LOGGER = LoggingUtils.getClassLogger();

	/**
	 * Constructor.
	 * 
	 * @param servicesProvider
	 *            the services provider
	 */
	public ConfigurationHandler(IRobotServiceProvider servicesProvider) {
		super(servicesProvider);
	}

	@Override
	public String getDeviceName() {
		return ConfigurationDevice.NAME;
	}

	@Override
	public void handleResult(RobotDeviceResult result) {
		if (result instanceof ConfigurationReadResult) {
			updateConfiguration((ConfigurationReadResult) result);
		}
	}

	/**
	 * Updates the configuration and notifies the match strategy handler of the
	 * change.
	 * 
	 * @param result
	 *            the data resulting from the configuration read
	 */
	protected void updateConfiguration(ConfigurationReadResult result) {
		LOGGER.fine("notifying configuration");
		IMatchStrategy strategy = servicesProvider
				.getService(IMatchStrategy.class);
		strategy.notifyEvent(new MatchConfigurationDone());
	}
}
