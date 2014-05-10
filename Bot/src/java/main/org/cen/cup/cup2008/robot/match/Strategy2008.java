package org.cen.cup.cup2008.robot.match;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.cen.logging.LoggingUtils;
import org.cen.robot.attributes.impl.RobotPosition;
import org.cen.robot.brain.TimeHandler;
import org.cen.robot.match.IMatchEvent;
import org.cen.robot.match.IMatchStrategy;
import org.cen.robot.match.IMatchStrategyHandler;
import org.cen.robot.match.MatchData;
import org.cen.robot.match.events.MatchConfigurationDone;
import org.cen.robot.match.events.MatchStartedEvent;
import org.cen.robot.services.IRobotServiceProvider;
import org.cen.robot.utils.RobotUtils;

public class Strategy2008 implements IMatchStrategyHandler {
	private static final Logger LOGGER = LoggingUtils.getClassLogger();

	private Strategy2008Context fsm;

	private IRobotServiceProvider servicesProvider;

	public void doConfiguration() {
		// new ConfigurationHandler(servicesProvider);
		new ContainerHandler(servicesProvider);
		new TimeHandler(servicesProvider);
		LOGGER.fine("Waiting for match configuration");
	}

	public void doWaitForMatchStart() {
		LOGGER.fine("Waiting for match to start");
	}

	public String getName() {
		return getClass().getSimpleName();
	}

	public boolean handleEvent(IMatchEvent event) {
		if (event instanceof MatchConfigurationDone) {
			LOGGER.fine("Configuration done");
			fsm.ConfigurationDone();
		} else if (event instanceof MatchStartedEvent) {
			LOGGER.fine("Match started");
			fsm.MatchStarted();
		} else if (event instanceof ObjectCollectedEvent) {
			LOGGER.fine("Object collected");
			fsm.Ok();
		} else if (event instanceof MatchStartedEvent) {
			LOGGER.fine("Match started");
			fsm.Ok();
		} else {
			return false;
		}
		return true;
	}

	@PostConstruct
	protected void initialize() {
		LOGGER.config("Initializing match strategy");
		fsm = new Strategy2008Context(this);
	}

	void setInitialPosition() {
		RobotPosition position = RobotUtils.getRobotAttribute(RobotPosition.class, servicesProvider);
		MatchData data = RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);
		switch (data.getSide()) {
		case RED:
			position.set(150, 2850, Math.toRadians(315));
			break;
		case VIOLET:
			position.set(150, 150, -Math.toRadians(315));
			break;
		}
	}

	public void setServicesProvider(IRobotServiceProvider provider) {
		servicesProvider = provider;
		IMatchStrategy strategy = servicesProvider.getService(IMatchStrategy.class);
		strategy.addHandler(this);
	}

	@Override
	public void start() {
		LOGGER.config("Match strategy started");
		fsm.Start();
	}

	public void unhandled() {
		LOGGER.warning("Unhandled transition: " + fsm.getTransition());
	}

	@Override
	public void stop() {
	}
}
