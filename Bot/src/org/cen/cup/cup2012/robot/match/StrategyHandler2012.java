package org.cen.cup.cup2012.robot.match;

import java.awt.geom.Point2D;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.cen.cup.cup2012.actions.GameActionMap2012;
import org.cen.cup.cup2012.actions.GameActionService2012;
import org.cen.cup.cup2012.device.arm2012.ArmHandler2012;
import org.cen.cup.cup2012.navigation.NavigationHandler2012;
import org.cen.logging.LoggingUtils;
import org.cen.navigation.AbstractDeadZoneHandler;
import org.cen.navigation.INavigationMap;
import org.cen.navigation.IPathVector;
import org.cen.navigation.ITrajectoryService;
import org.cen.navigation.Location;
import org.cen.navigation.PathVectorSpline;
import org.cen.robot.IRobotConfiguration;
import org.cen.robot.RobotPosition;
import org.cen.robot.RobotUtils;
import org.cen.robot.brain.CollisionHandler;
import org.cen.robot.brain.TimeHandler;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.RobotDeviceRequest;
import org.cen.robot.device.configuration.ConfigurationReadRequest;
import org.cen.robot.device.navigation.BezierMoveRequest;
import org.cen.robot.device.navigation.NavigationRequest;
import org.cen.robot.device.navigation.RotationRequest;
import org.cen.robot.device.navigation.SetInitialPositionRequest;
import org.cen.robot.device.timer.SleepRequest;
import org.cen.robot.match.AbstractMatchStrategyHandler;
import org.cen.robot.match.IMatchEvent;
import org.cen.robot.match.MatchData;
import org.cen.robot.match.MatchSide;
import org.cen.robot.match.events.CollisionDetectionEvent;
import org.cen.robot.match.events.MatchConfigurationDone;
import org.cen.robot.match.events.MatchFinishedEvent;
import org.cen.robot.match.events.MatchStartedEvent;
import org.cen.robot.match.events.OpponentMovedEvent;
import org.cen.robot.match.events.PositionReachedEvent;
import org.cen.robot.match.events.RobotInitializedEvent;
import org.cen.robot.match.events.TimerEvent;
import org.cen.robot.match.strategy.IGameStrategy;
import org.cen.robot.match.strategy.ITargetAction;
import org.cen.robot.match.strategy.ITargetActionItem;
import org.cen.robot.match.strategy.ITargetActionItemList;
import org.cen.robot.match.strategy.impl.DefaultNextGameStrategyItemComputer;
import org.cen.ui.gameboard.IGameBoardElement;
import org.cen.ui.gameboard.IGameBoardService;
import org.cen.ui.gameboard.elements.BezierCurve;
import org.cen.util.Holder;
import org.cen.util.StringConstants;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class StrategyHandler2012 extends AbstractMatchStrategyHandler implements ResourceLoaderAware {

	private static final double MINIMUM_ROTATION_ANGLE = Math.toRadians(5.0);

	private static final String ELEMENT_TRAJECTORY = "trajectory";

	private static final double DISTANCE_OBSTACLE = 400d;

	public static final String PROPERTY_INITIAL_POSITION = "initialPosition";

	private static final Logger LOGGER = LoggingUtils.getClassLogger();

	// gestion des collisions par les balises
	private static final boolean BEACON = false;

	private static final double ANGLE_START = Math.toRadians(22.5);

	// Distance minimum d'une cible au robot
	private static final double DISTANCE_TARGET_FROM_ROBOT = 350d;

	private CollisionHandler collisionHandler;

	private StrategyHandler2012Context fsm;

	private ConfigurationHandler2012 configurationHandler;

	private DefaultNextGameStrategyItemComputer nextGameStrategyItemComputer;

	private TimeHandler timeHandler;

	private NavigationHandler2012 navigationHandler;

	private ResourceLoader resourceLoader;

	private String resourcesPath;

	private AbstractDeadZoneHandler deadZoneHandler;

	private List<Location> currentPath;

	private MatchSide matchSide;

	private List<RobotDeviceRequest> moveToAnalyzeRequests;

	private ArmHandler2012 arm2012Handler;

	private GameActionService2012 gameActionService;

	private GameActionMap2012 gameActionMap;

	private List<Location> currentTrajectory;

	private ITargetAction currentTargetAction;

	private Iterator<ITargetActionItem> currentActions;

	private IGameStrategy buildStrategy() {
		GameStrategyFactory2012 factory = new GameStrategyFactory2012(servicesProvider, matchSide);
		IGameStrategy strategy = factory.getStrategy("Test");
		return strategy;
	}

	private void displayGainData(String gainData) {
		MatchData matchData = RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);
		matchData.put("gainData", gainData);
	}

	private void displayRequest(RobotDeviceRequest request) {
		IGameBoardService gameBoard = servicesProvider.getService(IGameBoardService.class);
		gameBoard.removeElements(ELEMENT_TRAJECTORY);
		if (request instanceof BezierMoveRequest) {
			RobotPosition position = RobotUtils.getRobotAttribute(RobotPosition.class, servicesProvider);
			BezierMoveRequest r = (BezierMoveRequest) request;
			Point2D start = position.getCentralPoint();
			Point2D end = r.getDestination();
			double cp1Distance = r.getD1() * 10.0;
			double cp2Distance = r.getD2() * 10.0;
			double initialAngle = position.getAlpha();
			double finalAngle = r.getAngle();
			BezierCurve element = new BezierCurve(ELEMENT_TRAJECTORY, start, end, cp1Distance, cp2Distance, initialAngle, finalAngle);
			List<IGameBoardElement> elements = gameBoard.getElements();
			elements.add(element);
		}
	}

	private void displayTrajectory(List<Location> locations) {
		if (locations == null) {
			return;
		}

		IGameBoardService gameBoard = servicesProvider.getService(IGameBoardService.class);
		gameBoard.removeElements(ELEMENT_TRAJECTORY);
		List<IGameBoardElement> elements = gameBoard.getElements();
		Location start = null;
		for (Location l : locations) {
			if (start != null) {
				IPathVector path = getPathVector(start, l);
				IGameBoardElement element = path.getGameBoardElement(ELEMENT_TRAJECTORY);
				elements.add(element);
			}
			start = l;
		}
	}

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

		configurationHandler = new ConfigurationHandler2012(servicesProvider);
		// deadZoneHandler = new DeadZoneHandler2012(servicesProvider);
		if (BEACON) {
			collisionHandler = new CollisionHandler(servicesProvider, deadZoneHandler);
		}
		navigationHandler = new NavigationHandler2012(servicesProvider);
		gameActionMap = new GameActionMap2012(servicesProvider);
		gameActionService = new GameActionService2012(servicesProvider);

		IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);

		// Demande de configuration
		LOGGER.fine("Waiting for match configuration");
		handler.sendRequest(new ConfigurationReadRequest());

		// writeLCD("PC Connected.");
		arm2012Handler = new ArmHandler2012(servicesProvider);

	}

	public void doHandleCollision() {
		// TODO Auto-generated method stub

	}

	public void doStartTrajectory() {
		currentTargetAction = null;
		currentTrajectory = null;
		currentActions = null;
		nextStep();
	}

	public void doStopTrajectory() {
		// TODO Auto-generated method stub

	}

	public void doWaitForMatchStart() {
		IGameStrategy strategy = buildStrategy();
		nextGameStrategyItemComputer = new DefaultNextGameStrategyItemComputer(servicesProvider, strategy);
		MatchData matchData = RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);
		matchData.setStrategy(strategy);
	}

	public void doWaitForRobotInitialization() {
		LOGGER.fine("Waiting for initialization");
		timeHandler = new TimeHandler(servicesProvider);
	}

	private void executeTargetActions() {
		if (currentActions == null) {
			ITargetActionItemList actions = currentTargetAction.getItems();
			currentActions = actions.iterator();
		}
		if (currentActions != null && currentActions.hasNext()) {
			ITargetActionItem action = currentActions.next();
			RobotDeviceRequest request = action.getRequest();
			sendRequest(request);
			displayRequest(request);
			if (!(request instanceof NavigationRequest) && !(request instanceof SleepRequest)) {
				nextStep();
			}
		} else {
			// the target is no more available
			LOGGER.fine("no more actions");
			currentTargetAction.getTarget().setAvailable(false);
			currentActions = null;
			currentTargetAction = null;
			currentTrajectory = null;
			displayRequest(null);
			nextStep();
		}
	}

	private void findNextTarget() {
		double time = timeHandler.getElapsedTime();
		Holder<List<Location>> path = new Holder<List<Location>>(null);
		StringBuilder gainData = new StringBuilder();
		RobotPosition position = RobotUtils.getRobotAttribute(RobotPosition.class, servicesProvider);
		ITrajectoryService trajectoryService = servicesProvider.getService(ITrajectoryService.class);
		INavigationMap map = trajectoryService.getNavigationMap();
		Location location = map.getNearestLocation(position.getCentralPoint());
		currentTargetAction = nextGameStrategyItemComputer.getNextTarget(time, location, path, gainData);
		currentTrajectory = path.getValue();
		displayTrajectory(currentTrajectory);
		displayGainData(gainData.toString());
	}

	private IPathVector getPathVector(Location start, Location end) {
		ITrajectoryService trajectoryService = servicesProvider.getService(ITrajectoryService.class);
		INavigationMap map = trajectoryService.getNavigationMap();
		IPathVector vector = map.getPathVector(start, end);
		return vector;
	}

	private RobotDeviceRequest getRequest(Location start, IPathVector path) {
		RobotDeviceRequest request = null;
		if (path instanceof PathVectorSpline) {
			PathVectorSpline p = (PathVectorSpline) path;
			Location end = p.getOtherEnd(start);
			double d1 = p.getControlPointDistance(start);
			double d2 = p.getControlPointDistance(end);
			double startAngle = p.getControlPointAngle(start);
			double endAngle = p.getControlPointAngle(end);

			// double diffAngle = getRotationAngle(startAngle);
			// boolean b = Math.abs(diffAngle) > MINIMUM_ROTATION_ANGLE;
			// if (b) {
			// request = new RotationRequest(diffAngle);
			// } else {
			request = new BezierMoveRequest(end.getPosition(), d1, d2, endAngle);
			// }
		}
		return request;
	}

	private double getRotationAngle(double startAngle) {
		RobotPosition position = RobotUtils.getRobotAttribute(RobotPosition.class, servicesProvider);
		double angle = position.getAlpha();
		double diffAngle = (startAngle - angle) % (Math.PI * 2d);
		if (diffAngle >= Math.PI) {
			diffAngle -= 2d * Math.PI;
		} else if (diffAngle <= Math.PI) {
			diffAngle += 2d * Math.PI;
		}
		return diffAngle;
	}

	private void handleCurrentTrajectory() {
		if (currentTrajectory.size() < 2) {
			// no more locations to reach
			currentTrajectory = null;
			nextStep();
			return;
		}

		Location start = currentTrajectory.get(0);
		Location end = currentTrajectory.get(1);
		IPathVector path = getPathVector(start, end);
		RobotDeviceRequest request = getRequest(start, path);
		sendRequest(request);
		if (!(request instanceof RotationRequest)) {
			currentTrajectory.remove(0);
		}
	}

	@Override
	public boolean handleEvent(IMatchEvent event) {
		if (event instanceof RobotInitializedEvent) {
			LOGGER.fine("Robot initialized");
			// startReceived = false;
			fsm.RobotInitializationDone();
		} else if (event instanceof MatchConfigurationDone) {
			LOGGER.fine("Configuration done");
			setMatchSide();
			// setVision();
			fsm.ConfigurationDone();
		} else if (event instanceof CollisionDetectionEvent) {
			LOGGER.fine("Collision event");
			// handleCollision((CollisionDetectionEvent) event);
		} else if (event instanceof OpponentMovedEvent) {
			// handleOpponentMoved((OpponentMovedEvent) event);
		} else if (event instanceof MatchStartedEvent) {
			LOGGER.fine("Match started");
			// startReceived = true;
			fsm.MatchStarted();
		} else if (event instanceof MatchFinishedEvent) {
			LOGGER.fine("Match stopped");
			fsm.MatchStopped();
		} else if (event instanceof PositionReachedEvent) {
			nextStep();
			// } else if (event instanceof MoveStoppedEvent) {
			// LOGGER.fine("Move stopped");
			// if (!visionEnabled) {
			// sequenceIndex = lastSequenceIndex;
			// }
		} else if (event instanceof TimerEvent) {
			nextStep();
		}
		return true;
	}

	private void nextStep() {
		runMainLoop();
	}

	private void runMainLoop() {
		if (currentActions != null) {
			LOGGER.fine("executeTargetActions 1");
			// actions pending
			executeTargetActions();
		} else if (currentTrajectory != null) {
			LOGGER.fine("handleCurrentTrajectory");
			// trajectory if any
			handleCurrentTrajectory();
		} else if (currentTargetAction == null) {
			LOGGER.fine("findNextTarget");
			// no target, search a new one
			findNextTarget();
			if (currentTrajectory != null) {
				// next location
				nextStep();
			}
		} else {
			LOGGER.fine("executeTargetActions 2");
			// executes the actions of the active target
			executeTargetActions();
		}
	}

	private void sendRequest(RobotDeviceRequest request) {
		IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
		handler.sendRequest(request);
	}

	public void setInitialPosition() {
		RobotPosition position = RobotUtils.getRobotAttribute(RobotPosition.class, servicesProvider);
		MatchData data = RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);
		IRobotConfiguration configuration = RobotUtils.getRobot(servicesProvider).getConfiguration();
		Properties properties = configuration.getProperties();
		position.setFromProperties(properties, PROPERTY_INITIAL_POSITION + "." + data.getSide() + ".");

		// Envoi de la position initiale à la carte mère
		IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
		Point2D cp = position.getCentralPoint();
		handler.sendRequest(new SetInitialPositionRequest(cp.getX(), cp.getY(), position.getAlpha()));

		String s = StringConstants.STR_CLS + "Side: ";
		switch (data.getSide()) {
		case RED:
			s += "RED";
			break;
		case VIOLET:
			s += "VIOLET";
			break;
		}
		// writeLCD(s);
	}

	private void setMatchSide() {
		MatchData2012 data = (MatchData2012) RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);
		matchSide = data.getSide();
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
		fsm = new StrategyHandler2012Context(this);
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
			// n'arrête pas le TimeHandler pout traiter le redémarrage
			// timeHandler.shutdown();
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
		if (fsm != null) {
			fsm = null;
		}
	}

	public void unhandled() {
		// TODO Auto-generated method stub

	}
}
