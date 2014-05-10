package org.cen.robot.match;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.cen.logging.LoggingUtils;
import org.cen.robot.services.IRobotServiceProvider;

public abstract class AbstractMatchStrategyHandler implements IMatchStrategyHandler {

	protected IRobotServiceProvider servicesProvider;

	private static final Logger LOGGER = LoggingUtils.getClassLogger();

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	@PostConstruct
	public void initialize() {
		LOGGER.fine("initializing match strategy handler");
	}

	private void registerHandler() {
		if (servicesProvider == null) {
			LOGGER.warning("match strategy handler cannot be registered");
			return;
		}

		IMatchStrategy strategy = servicesProvider.getService(IMatchStrategy.class);
		strategy.addHandler(this);
		LOGGER.finest("match strategy handler registered");
	}

	@Override
	public void setServicesProvider(IRobotServiceProvider provider) {
		servicesProvider = provider;
		registerHandler();
	}
}
