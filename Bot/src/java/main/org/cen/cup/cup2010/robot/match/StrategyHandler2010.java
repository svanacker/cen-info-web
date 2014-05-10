package org.cen.cup.cup2010.robot.match;

import org.cen.geom.Point2D;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import org.cen.cup.cup2010.navigation.NavigationHandler2010;
import org.cen.logging.LoggingUtils;
import org.cen.navigation.DefaultDeadZoneHandler;
import org.cen.robot.attributes.impl.RobotPosition;
import org.cen.robot.brain.CollisionHandler;
import org.cen.robot.brain.TimeHandler;
import org.cen.robot.configuration.IRobotConfiguration;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.configuration.ConfigurationReadRequest;
import org.cen.robot.match.AbstractMatchStrategyHandler;
import org.cen.robot.match.IMatchEvent;
import org.cen.robot.match.MatchData;
import org.cen.robot.match.Opponent;
import org.cen.robot.match.events.CollisionDetectionEvent;
import org.cen.robot.match.events.MatchConfigurationDone;
import org.cen.robot.match.events.MatchFinishedEvent;
import org.cen.robot.match.events.MatchStartedEvent;
import org.cen.robot.match.events.RobotInitializedEvent;
import org.cen.robot.utils.RobotUtils;
import org.cen.util.StringConstants;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * Implementation of the strategy handler for the cup 2010.
 * 
 * @author Emmanuel ZURMELY
 */
public class StrategyHandler2010 extends AbstractMatchStrategyHandler implements
		ResourceLoaderAware {
	public static final String PROPERTY_INITIAL_POSITION = "initialPosition";

	private static final Logger LOGGER = LoggingUtils.getClassLogger();

	private CollisionHandler collisionHandler;

	private StrategyHandler2010Context fsm;

	private ConfigurationHandler2010 configurationHandler;

	private TimeHandler timeHandler;

	private NavigationHandler2010 navigationHandler;

	private ResourceLoader resourceLoader;

	private String resourcesPath;

	private DefaultDeadZoneHandler deadZoneHandler;

	private Specific2010Handler specific2010Handler;

	public void doConfiguration() {
		Properties properties = new Properties();
		Resource resource = resourceLoader.getResource(resourcesPath);
		try {
			InputStream is = resource.getInputStream();
			try {
				properties.load(is);
			} finally {
				is.close();
			}
		} catch (Exception e) {
			LOGGER.warning("unable to load properties: " + e.getMessage());
		}

		configurationHandler = new ConfigurationHandler2010(servicesProvider);
		deadZoneHandler = new DefaultDeadZoneHandler(servicesProvider);
		collisionHandler = new CollisionHandler(servicesProvider,
				deadZoneHandler);
		navigationHandler = new NavigationHandler2010(servicesProvider);
		specific2010Handler = new Specific2010Handler(servicesProvider);
		navigationHandler.setProperties(properties);
		LOGGER.fine("Waiting for match configuration");
		IRobotDevicesHandler handler = servicesProvider
				.getService(IRobotDevicesHandler.class);
		handler.getRequestDispatcher().sendRequest(
				new ConfigurationReadRequest());
		// writeLCD("PC Connected");
	}

	public void doStartTrajectory() {
		LOGGER.fine("Starting trajectory");
		navigationHandler.startTrajectory();
	}

	public void doStopTrajectory() {
		LOGGER.fine("Stopping trajectory");
		stop();
	}

	public void doWaitForMatchStart() {
		navigationHandler.handleConfigurationDone();
		LOGGER.fine("Waiting for match start");
		// TODO Auto-generated method stub
	}

	public void doWaitForRobotInitialization() {
		LOGGER.fine("Waiting for initialization");
		timeHandler = new TimeHandler(servicesProvider);
	}

	private void handleCollectDone() {
		navigationHandler.handleCollectDone();
	}

	private void handleCollision(CollisionDetectionEvent event) {
		Opponent opponent = RobotUtils.getRobotAttribute(Opponent.class,
				servicesProvider);
		Point2D position = event.getPosition();
		opponent.setLastLocation(position);

		deadZoneHandler.handleDeadZone(position);

		if (navigationHandler != null) {
			navigationHandler.handleCollision();
		}
	}

	private void handleCornFixed() {
		navigationHandler.handleCornFixed();
	}

	@Override
	public boolean handleEvent(IMatchEvent event) {
		if (event instanceof RobotInitializedEvent) {
			LOGGER.fine("Robot initialized");
			fsm.RobotInitializationDone();
		} else if (event instanceof MatchConfigurationDone) {
			LOGGER.fine("Configuration done");
			fsm.ConfigurationDone();
		} else if (event instanceof CollisionDetectionEvent) {
			LOGGER.fine("Collision detected");
			handleCollision((CollisionDetectionEvent) event);
		} else if (event instanceof MatchStartedEvent) {
			LOGGER.fine("Match started");
			fsm.MatchStarted();
		} else if (event instanceof MatchFinishedEvent) {
			LOGGER.fine("Match stopped");
			fsm.MatchStopped();
		} else if (event instanceof CornFixedEvent) {
			handleCornFixed();
		} else if (event instanceof CollectDoneEvent) {
			handleCollectDone();
		}
		return true;
	}

	public void setInitialPosition() {
		RobotPosition position = RobotUtils.getRobotAttribute(
				RobotPosition.class, servicesProvider);
		MatchData data = RobotUtils.getRobotAttribute(MatchData.class,
				servicesProvider);
		IRobotConfiguration configuration = RobotUtils.getRobot(
				servicesProvider).getConfiguration();
		Properties properties = configuration.getProperties();
		position.setFromProperties(properties, PROPERTY_INITIAL_POSITION + "."
				+ data.getSide() + ".");

		String s = StringConstants.STR_CLS + "Side: ";
		switch (data.getSide()) {
		case RED:
			s += "BLUE";
			break;
		case VIOLET:
			s += "YELLOW";
			break;
		}
		// writeLCD(s);
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public void setResourcesPath(String resourcesPath) {
		this.resourcesPath = resourcesPath;
	}

	@Override
	public void start() {
		LOGGER.config("Starting match strategy");
		fsm = new StrategyHandler2010Context(this);
		LOGGER.config("Match strategy started");
		fsm.Start();
	}

	@Override
	public void stop() {
		LOGGER.config("Stopping match strategy");
		if (collisionHandler != null) {
			collisionHandler.shutdown();
			collisionHandler = null;
		}
		if (configurationHandler != null) {
			configurationHandler.shutdown();
			configurationHandler = null;
		}
		if (timeHandler != null) {
			timeHandler.shutdown();
			timeHandler = null;
		}
		if (navigationHandler != null) {
			navigationHandler.shutdown();
			navigationHandler = null;
		}
		if (deadZoneHandler != null) {
			deadZoneHandler.shutdown();
			deadZoneHandler = null;
		}
		if (specific2010Handler != null) {
			specific2010Handler.shutdown();
			specific2010Handler = null;
		}
		if (fsm != null) {
			fsm = null;
		}
	}

	public void unhandled() {
		LOGGER.warning("Unhandled transition: " + fsm.getTransition());
	}
}
