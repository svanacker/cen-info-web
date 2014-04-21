package org.cen.cup.cup2010.navigation;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import org.cen.com.IComService;
import org.cen.cup.cup2010.device.specific2010.CollectCorn2010Request;
import org.cen.cup.cup2010.device.specific2010.CollectCorn2010Request.Side;
import org.cen.cup.cup2010.device.specific2010.CollectTomato2010Request;
import org.cen.cup.cup2010.device.specific2010.CollectTomato2010Request.Action;
import org.cen.cup.cup2010.device.specific2010.Specific2010SleepRequest;
import org.cen.cup.cup2010.device.specific2010.com.CollectTomato2010OutData;
import org.cen.cup.cup2010.gameboard.elements.ControlPointMarker;
import org.cen.logging.LoggingUtils;
import org.cen.math.Angle;
import org.cen.navigation.ControlPoint;
import org.cen.navigation.INavigationMap;
import org.cen.navigation.IPathVector;
import org.cen.navigation.ITrajectoryService;
import org.cen.navigation.Location;
import org.cen.navigation.LocationGroup;
import org.cen.navigation.LocationGroupStats;
import org.cen.navigation.NavigationStrategyHandler;
import org.cen.navigation.PathSegment;
import org.cen.navigation.TrajectoryPathElement;
import org.cen.navigation.TrajectoryPathElementComparator;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.RobotPosition;
import org.cen.robot.RobotUtils;
import org.cen.robot.brain.AbstractDeviceHandler;
import org.cen.robot.device.DeviceRequestDispatcher;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.RobotDeviceRequest;
import org.cen.robot.device.RobotDeviceResult;
import org.cen.robot.device.navigation.CollisionDetectionRequest;
import org.cen.robot.device.navigation.MoveRequest;
import org.cen.robot.device.navigation.NavigationDevice;
import org.cen.robot.device.navigation.NavigationInitializeRequest;
import org.cen.robot.device.navigation.NavigationRequest;
import org.cen.robot.device.navigation.NavigationResult;
import org.cen.robot.device.navigation.StopRequest;
import org.cen.robot.device.navigation.WaitPositionUpdateRequest;
import org.cen.robot.device.navigation.com.StopOutData;
import org.cen.robot.match.MatchData;
import org.cen.robot.match.MatchSide;
import org.cen.ui.gameboard.IGameBoardService;

public class NavigationHandler2010 extends AbstractDeviceHandler {
	private static final String PROPERTY_REQUESTS = "requests";

	private static final String PROPERTY_CONTROLPOINTS = "controlPoints";

	private static final Logger LOGGER = LoggingUtils.getClassLogger();

	private static final String PROPERTY_ASYNCHRONOUS = "asynchronous";

	private static final String PROPERTY_ASYNCHRONOUS_DEFAULT_VALUE = "true";

	private static final String PROPERTY_TRAJECTORY = "trajectory";

	private static final double DISTANCE_THRESHOLD = 20;

	private static final double DISTANCE_PROBE = 120;

	private static final int DURATION_RELEASE = 3000;

	private static final double ANGLE_ALIGNED = .1;

	private static final double DISTANCE_FINAL_POINT = 100;

	private static final int DISTANCE_GOAL_BACKWARD = 160;

	private static final int DURATION_TOMATO_ON = 50;

	private boolean asynchronous = true;

	private int nextRequest;

	private Properties properties;

	private BlockingQueue<RobotDeviceRequest> queue = new ArrayBlockingQueue<RobotDeviceRequest>(
			5, true);

	private List<RobotDeviceRequest> requests;

	private Thread sender;

	protected boolean terminated = false;

	private NavigationStrategyHandler strategyHandler;

	private LocationGroup currentObjective;

	private Corn2010[] corns;

	private Tomato2010[] tomatoes;

	private Map<String, Location> mapLocations = new HashMap<String, Location>();

	private Set<PathSegment> pathSegments = new HashSet<PathSegment>();

	private TomatoControlPoint2010[] tomatoControlPoints;

	private CornControlPoint2010[] cornControlPoints;

	private List<TrajectoryPathElement> currentPathElements;

	private int currentPathElementsIndex;

	private Location goal;

	private boolean goingToGoal = false;

	int cornsCollected = 0;

	int tomatoesCollected = 0;

	private Corn2010 currentCorn = null;

	boolean navigationSuspended = false;

	private boolean goalTrajectory = false;

	public NavigationHandler2010(IRobotServiceProvider servicesProvider) {
		super(servicesProvider);
		initialize();
	}

	private void addCommands(List<RobotDeviceRequest> requests,
			TrajectoryPathElement pathElement, Point2D centralPoint,
			Set<TomatoControlPoint2010> tomatoes) {
		ControlPoint cp = pathElement.getControlPoint();
		if (cp != null && cp.isEnabled()) {
			if (cp instanceof TomatoControlPoint2010) {
				TomatoControlPoint2010 tcp = (TomatoControlPoint2010) cp;
				if (tcp.isFinalPoint()) {
					int n = requests.size();
					RobotDeviceRequest r = requests.get(n - 1);
					if (r instanceof MoveRequest) {
						MoveRequest mr = (MoveRequest) r;
						requests.remove(n - 1);
						double distance = mr.getDistance()
								- DISTANCE_FINAL_POINT;
						requests.add(new MoveRequest(distance));
						requests.add(new MoveRequest(-distance));
						// retire le point de la trajectoire
						if (currentPathElementsIndex < currentPathElements
								.size()) {
							currentPathElements
									.remove(currentPathElementsIndex);
						}
					}
				}
			} else if (cp instanceof CornControlPoint2010) {
				// Position réelle du maïs
				Point2D p1 = cp.getPoint();
				// Position de ramassage
				Point2D p2 = pathElement.getPosition();
				// Calcul de la position du maïs par rapport à la trajectoire
				double angle = Angle.getPointsAngle(centralPoint, p2, p1);
				// Côté gauche ou droit
				Side side;
				if (angle > 0) {
					side = Side.LEFT;
				} else {
					side = Side.RIGHT;
				}
				// Calcul des positions
				int n = requests.size();
				RobotDeviceRequest r = requests.get(n - 1);
				if (r instanceof MoveRequest) {
					// Marque la cible courante
					CornControlPoint2010 ccp = (CornControlPoint2010) cp;
					currentCorn = ccp.getTarget();
					// Ajustement de la trajectoire
					MoveRequest mr = (MoveRequest) r;
					double distance = mr.getDistance();
					requests.remove(n - 1);
					// Position de sondage
					requests.add(new MoveRequest(distance - DISTANCE_PROBE));
					// Place le pilote de navigation en mode mouvement afin
					// qu'il
					// traite le déplacement
					requests.add(new WaitPositionUpdateRequest());
					// Séquence de collecte
					requests.add(new CollectCorn2010Request(
							side,
							org.cen.cup.cup2010.device.specific2010.CollectCorn2010Request.Action.SEQUENCE));
					// // Test du maïs
					// requests.add(new CollectCorn2010Request(side,
					// org.cen.cup.cup2010.device.specific2010.CollectCorn2010Request.Action.DOWN));
					// // Position de collecte
					// requests.add(new MoveRequest(DISTANCE_COLLECT));
					// // Collecte
					// requests.add(new CollectCorn2010Request(side,
					// org.cen.cup.cup2010.device.specific2010.CollectCorn2010Request.Action.COLLECT));
				}
			}
		}
		if (!tomatoes.isEmpty()) {
			// Marque les cibles
			for (TomatoControlPoint2010 tcp : tomatoes) {
				Tomato2010 currentTomato = tcp.getTarget();
				currentTomato.setAvailable(false);
				// Met en marche le rouleau avant d'approcher la tomate
				// requests.add(0, new
				// Specific2010SleepRequest(DURATION_TOMATO_ON));
				// requests.add(0, new CollectTomato2010Request(Action.ON));
			}
		}
	}

	private void addCornControlPoint(int index, String location,
			String... segments) {
		corns[index].setName(location);
		Location l = getMapLocation(location);
		CornControlPoint2010 cp = new CornControlPoint2010(l.getPosition(), "",
				corns[index]);
		for (String segment : segments) {
			String[] s = segment.split(",");
			if (s.length != 2) {
				LOGGER.severe("Invalid segment: " + segment);
				return;
			}
			PathSegment ps = findSegment(s[0], s[1]);
			if (ps == null) {
				LOGGER.severe("Invalid segment: " + segment);
				return;
			}
			ps.getControlPoints().add(cp);
		}

		cornControlPoints[index] = cp;
	}

	private void addTomatoControlPoint(int index, String location) {
		addTomatoControlPoint(index, location, false);
	}

	private void addTomatoControlPoint(int index, String location,
			boolean finalPoint) {
		Location l = getMapLocation(location);
		if (l == null) {
			// avertissement dans getMapLocation
			return;
		}

		TomatoControlPoint2010 cp = new TomatoControlPoint2010(l.getPosition(),
				"", tomatoes[index], finalPoint);
		for (PathSegment s : pathSegments) {
			if (s.contains(l)) {
				s.getControlPoints().add(cp);
			}
		}

		tomatoControlPoints[index] = cp;
	}

	private void buildControlPoints() {
		IGameBoardService gameBoard = servicesProvider
				.getService(IGameBoardService.class);
		gameBoard.removeElements(PROPERTY_CONTROLPOINTS);

		Collection<Point2D> controlPoints = new ArrayList<Point2D>();
		for (PathSegment segment : currentObjective.getSegments()) {
			for (ControlPoint controlPoint : segment.getControlPoints()) {
				if (controlPoint.isEnabled()) {
					Point2D p = controlPoint.getSegmentPoint(segment);
					controlPoints.add(p);
					gameBoard.getElements().add(
							new ControlPointMarker(PROPERTY_CONTROLPOINTS, p));
				}
			}
		}

		MatchData data = RobotUtils.getRobotAttribute(MatchData.class,
				servicesProvider);
		data.put(PROPERTY_CONTROLPOINTS, controlPoints);
	}

	private void computeNextRequests() {
		if (currentPathElements == null
				|| currentPathElementsIndex >= currentPathElements.size()) {
			trajectoryDone();
			return;
		}

		if (cornsCollected >= 3 && !goingToGoal) {
			Point2D p = getNextSegmentEnd();
			goToGoal(p);
			return;
		}

		LOGGER.finest("computing next trajectory");
		RobotPosition position = RobotUtils.getRobotAttribute(
				RobotPosition.class, servicesProvider);
		Point2D centralPoint = position.getCentralPoint();
		Point2D p;
		ControlPoint cp;
		TrajectoryPathElement pathElement;
		Set<TomatoControlPoint2010> tomatoes = new HashSet<TomatoControlPoint2010>();
		// Si on est trop proche du point à atteindre, on passe au point
		// suivant
		while (true) {
			if (currentPathElementsIndex >= currentPathElements.size()) {
				trajectoryDone();
				return;
			}
			pathElement = currentPathElements.get(currentPathElementsIndex++);
			p = pathElement.getPosition();
			cp = pathElement.getControlPoint();
			if (cp instanceof TomatoControlPoint2010) {
				TomatoControlPoint2010 tcp = (TomatoControlPoint2010) cp;
				tomatoes.add(tcp);
			}
			if ((cp == null || !cp.mustStop() || !cp.isEnabled())
					&& isNextPointAligned(centralPoint, p)) {
				continue;
			}
			if (p.distance(centralPoint) >= DISTANCE_THRESHOLD) {
				break;
			}
		}

		// Calcul de la trajectoire
		List<RobotDeviceRequest> requests = goToPoint(p);
		addCommands(requests, pathElement, centralPoint, tomatoes);

		nextRequest = 0;
		this.requests = requests;
	}

	private Point2D getNextSegmentEnd() {
		int i = currentPathElementsIndex + 1;
		if (i < currentPathElements.size()) {
			TrajectoryPathElement e = currentPathElements.get(i);
			Location l = e.getSegmentEnd();
			return l.getPosition();
		} else {
			return null;
		}
	}

	private void disableCorns() {
		for (Corn2010 c : corns) {
			c.setAvailable(false);
		}
	}

	private void disableTomatoes() {
		for (Tomato2010 t : tomatoes) {
			t.setAvailable(false);
		}
	}

	private PathSegment findSegment(Location start, Location end) {
		for (PathSegment s : pathSegments) {
			if (s.isSameSegment(start, end)) {
				return s;
			}
		}
		return null;
	}

	private PathSegment findSegment(String start, String end) {
		Location ls = mapLocations.get(start);
		Location le = mapLocations.get(end);
		return findSegment(ls, le);
	}

	@Override
	public String getDeviceName() {
		return NavigationDevice.NAME;
	}

	private Location getMapLocation(String location) {
		Location l = mapLocations.get(location);
		if (l == null) {
			LOGGER.severe("Invalid location: " + location);
		}
		return l;
	}

	private Point2D getMapLocationPoint(String location) {
		return getMapLocation(location).getPosition();
	}

	private List<TrajectoryPathElement> getSortedControlPoints(Location start,
			PathSegment segment) {
		List<TrajectoryPathElement> list = new ArrayList<TrajectoryPathElement>();
		for (ControlPoint cp : segment.getControlPoints()) {
			Point2D p = cp.getSegmentPoint(segment);
			if (!Double.isNaN(p.getX()) && !Double.isNaN(p.getY())) {
				list.add(new TrajectoryPathElement(p, cp, segment.getStart(),
						segment.getEnd()));
			}
		}
		Collections.sort(list,
				new TrajectoryPathElementComparator(start.getPosition()));
		return list;
	}

	private List<TrajectoryPathElement> getTrajectoryPath(List<Location> path) {
		List<TrajectoryPathElement> result = new ArrayList<TrajectoryPathElement>();
		Location start = null;
		for (Location l : path) {
			if (start != null) {
				if (result.isEmpty()) {
					result.add(new TrajectoryPathElement(start.getPosition(),
							null, start, l));
				}
				if (!goingToGoal) {
					PathSegment s = findSegment(start, l);
					if (s != null) {
						List<TrajectoryPathElement> list = getSortedControlPoints(
								start, s);
						for (TrajectoryPathElement e : list) {
							ControlPoint cp = e.getControlPoint();
							if (cp != null && cp.isEnabled()) {
								result.add(e);
							}
						}
					}
				}
				result.add(new TrajectoryPathElement(l.getPosition(), null,
						start, l));
			}
			start = l;
		}
		return result;
	}

	private void goToGoal() {
		goToGoal(null);
	}

	private void goToGoal(Point2D p) {
		LOGGER.finest("going to goal");
		goingToGoal = true;

		// Construction des requêtes
		if (p == null) {
			RobotPosition position = RobotUtils.getRobotAttribute(
					RobotPosition.class, servicesProvider);
			p = position.getCentralPoint();
		}
		ITrajectoryService trajectoryService = servicesProvider
				.getService(ITrajectoryService.class);
		List<Location> path = trajectoryService.getPath(p, goal.getPosition());

		// Récupère les éléments de trajectoire
		currentPathElements = getTrajectoryPath(path);
		currentPathElementsIndex = 0;

		computeNextRequests();
	}

	private List<RobotDeviceRequest> goToPoint(Point2D point) {
		LOGGER.finest("going to point " + point);
		RobotPosition position = RobotUtils.getRobotAttribute(
				RobotPosition.class, servicesProvider);
		Point2D centralPoint = position.getCentralPoint();

		// Chemin au point suivant
		List<Point2D> points = new ArrayList<Point2D>();
		points.add(centralPoint);
		points.add(point);

		// Construction des requêtes
		List<RobotDeviceRequest> requests = new ArrayList<RobotDeviceRequest>();
		ITrajectoryService trajectoryService = servicesProvider
				.getService(ITrajectoryService.class);
		trajectoryService.buildTrajectoryRequests(points, position.getAlpha(),
				requests, false);
		for (RobotDeviceRequest r : requests) {
			System.out.println(r);
		}

		return requests;
	}

	public void handleCollectDone() {
		resumeNavigation();
	}

	public void handleCollision() {
		reset();
		// trouve un nouveau parcourt
		if (goingToGoal) {
			goToGoal();
		} else {
			selectLocationGroup();
		}
		// indique au device que la collision a été gérée
		queue.offer(new CollisionDetectionRequest());
		sendNextRequest();
	}

	public void handleConfigurationDone() {
		initializeLocationGroups();
		disableCorns();
		selectLocationGroup();
	}

	public void handleCornFixed() {
		if (currentCorn != null) {
			currentCorn.setAvailable(false);
			LOGGER.fine("corn fixed " + currentCorn);
		}
	}

	@Override
	public void handleResult(RobotDeviceResult result) {
		if (result instanceof NavigationResult) {
			NavigationResult r = (NavigationResult) result;
			switch (r.getStatus()) {
			case COLLISION:
				break;
			case EMPTYQUEUE:
				break;
			case ENQUEUED:
				break;
			case INTERRUPTED:
				break;
			case MOVING:
				break;
			case REACHED:
				sendNextRequest();
				break;
			case STOPPED:
				break;
			}
		}
	}

	private void handleSentRequest(RobotDeviceRequest r) {
		if (r instanceof CollectCorn2010Request) {
			setCurrentCornCollected();
			// attends la fin de la collecte
			suspendNavigation();
		} else if (r instanceof Specific2010SleepRequest) {
			Specific2010SleepRequest sr = (Specific2010SleepRequest) r;
			synchronized (sr) {
				try {
					Thread.sleep(sr.getDuration());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		if (!waitForResult(r)) {
			sendNextRequest();
		}
	}

	private void initialize() {
		// Indexation des positions de la carte
		ITrajectoryService trajectoryService = servicesProvider
				.getService(ITrajectoryService.class);
		INavigationMap map = trajectoryService.getNavigationMap();
		for (Location l : map.getLocations()) {
			mapLocations.put(l.getName(), l);
		}

		// Modélisation tomates et maïs
		corns = new Corn2010[18];
		for (int i = 0; i < corns.length; i++) {
			corns[i] = new Corn2010();
		}
		tomatoes = new Tomato2010[14];
		for (int i = 0; i < tomatoes.length; i++) {
			tomatoes[i] = new Tomato2010();
		}

		// Construit les segments à partir du maillage
		Collection<IPathVector> vectors = map.getPathVectors();
		for (IPathVector v : vectors) {
			pathSegments.add(new PathSegment(v.getStart(), v.getEnd()));
		}

		// Ajout des points de contrôle aux segments
		tomatoControlPoints = new TomatoControlPoint2010[14];
		addTomatoControlPoint(0, "R0C1", true);
		addTomatoControlPoint(1, "R0C3", true);
		addTomatoControlPoint(2, "R2C2");
		addTomatoControlPoint(3, "R2C4");
		addTomatoControlPoint(4, "R4C3");
		addTomatoControlPoint(5, "R4C5", true);
		addTomatoControlPoint(6, "R6C2");
		addTomatoControlPoint(7, "R6C4");
		addTomatoControlPoint(8, "R8C3");
		addTomatoControlPoint(9, "R8C5", true);
		addTomatoControlPoint(10, "R10C2");
		addTomatoControlPoint(11, "R10C4");
		addTomatoControlPoint(12, "R12C1", true);
		addTomatoControlPoint(13, "R12C3", true);

		cornControlPoints = new CornControlPoint2010[18];
		addCornControlPoint(0, "R0C0");
		addCornControlPoint(1, "R0C2", "R0C3,R2C2", "R2C2,R0C1");
		addCornControlPoint(2, "R0C4");
		addCornControlPoint(3, "R2C1", "R2C0,R4C1", "R4C1,R2C2", "R2C2,R0C1");
		addCornControlPoint(4, "R2C3", "R0C3,R2C2", "R2C2,R4C3", "R4C3,R2C4");
		addCornControlPoint(5, "R2C5", "R2C4,yellow end");
		addCornControlPoint(6, "R4C2", "R2C2,R4C1", "R4C1,R6C2", "R6C2,R4C3",
				"R4C3,R2C2");
		addCornControlPoint(7, "R4C4", "R2C4,R4C3", "R4C3,R6C4", "R6C4,R4C5");
		addCornControlPoint(8, "R6C3", "R4C3,R6C2", "R6C2,R8C3", "R8C3,R6C4",
				"R6C4,R4C3");
		addCornControlPoint(9, "R6C5", "R4C5,R6C4", "R6C4,R8C5");
		addCornControlPoint(10, "R8C2", "R6C2,R8C1", "R8C1,R10C2",
				"R10C2,R8C3", "R8C3,R6C2");
		addCornControlPoint(11, "R8C4", "R8C5,R6C4", "R6C4,R8C3", "R8C3,R10C4");
		addCornControlPoint(12, "R10C1", "R10C0,R8C1", "R8C1,R10C2",
				"R10C2,R12C1");
		addCornControlPoint(13, "R10C3", "R10C4,R8C3", "R8C3,R10C2",
				"R10C2,R12C3");
		addCornControlPoint(14, "R10C5", "R10C4,blue end");
		addCornControlPoint(15, "R12C0");
		addCornControlPoint(16, "R12C2", "R12C3,R10C2", "R10C2,R12C1");
		addCornControlPoint(17, "R12C4");
	}

	private void initializeLocationGroups() {
		ITrajectoryService trajectoryService = servicesProvider
				.getService(ITrajectoryService.class);
		strategyHandler = new NavigationStrategyHandler(trajectoryService);

		MatchData data = RobotUtils.getRobotAttribute(MatchData.class,
				servicesProvider);
		// poids de la proximité des buts
		int blueGoal = 1;
		if (data.getSide() != MatchSide.RED) {
			blueGoal = -blueGoal;
		}
		int yellowGoal = -blueGoal;
		// strategyHandler.addLocationGroup(pathSegments, new
		// LocationGroup2010("test", 0), "R10C0", "R8C1");
		strategyHandler.addLocationGroup(pathSegments, new LocationGroup2010(
				"blue central", blueGoal * 2), "R2C0", "R4C1", "R6C2", "R8C3",
				"R10C4", "blue end");
		strategyHandler.addLocationGroup(pathSegments, new LocationGroup2010(
				"blue left", 0), "R10C2", "R12C3");
		strategyHandler.addLocationGroup(pathSegments, new LocationGroup2010(
				"blue right", yellowGoal), "R0C1", "R2C2", "R4C3", "R6C4",
				"R8C5");
		strategyHandler.addLocationGroup(pathSegments, new LocationGroup2010(
				"yellow central", yellowGoal * 2), "R10C0", "R8C1", "R6C2",
				"R4C3", "R2C4", "yellow end");
		strategyHandler.addLocationGroup(pathSegments, new LocationGroup2010(
				"yellow right", 0), "R2C2", "R0C3");
		strategyHandler.addLocationGroup(pathSegments, new LocationGroup2010(
				"yellow left", blueGoal), "R12C1", "R10C2", "R8C3", "R6C4",
				"R4C5");

		if (data.getSide() == MatchSide.RED) {
			goal = mapLocations.get("blue container");
		} else {
			goal = mapLocations.get("yellow container");
		}
	}

	public boolean isAsynchronous() {
		return asynchronous;
	}

	private boolean isNextPointAligned(Point2D o, Point2D p1) {
		if (currentPathElementsIndex >= currentPathElements.size()) {
			return false;
		}
		TrajectoryPathElement pathElement = currentPathElements
				.get(currentPathElementsIndex);
		Point2D p2 = pathElement.getPosition();
		double angle = Angle.getPointsAngle(o, p1, p2);
		return Math.abs(angle) < Math.toRadians(ANGLE_ALIGNED);
	}

	private void reset() {
		// Réinitialisation de l'objectif courant
		requests = null;
		nextRequest = 0;
		currentObjective = null;
		currentPathElementsIndex = 0;
		currentPathElements = null;
	}

	private void resumeNavigation() {
		if (navigationSuspended) {
			navigationSuspended = false;
			sendNextRequest();
		}
	}

	private void selectLocationGroup() {
		LOGGER.fine("selecting new objective");
		goingToGoal = false;

		RobotPosition position = RobotUtils.getRobotAttribute(
				RobotPosition.class, servicesProvider);
		Point2D centralPoint = position.getCentralPoint();
		List<LocationGroupStats> results = strategyHandler.analyzeGroups(
				centralPoint, position.getAlpha());

		for (LocationGroupStats s : results) {
			System.out.println(s);
		}

		LocationGroupStats bestGroup = results.get(0);
		currentObjective = bestGroup.getGroup();
		List<Location> currentPath = bestGroup.getPath();

		for (Location l : currentPath) {
			System.out.println(l);
		}

		LOGGER.config("best locations group: " + currentObjective.getName());

		if (currentPath == null) {
			LOGGER.severe("No path defined");
			return;
		}

		// Récupère les éléments de trajectoire
		currentPathElements = getTrajectoryPath(currentPath);
		currentPathElementsIndex = 0;

		String pointsNames = "";
		for (Location l : currentPath) {
			if (!pointsNames.isEmpty()) {
				pointsNames += ",";
			}
			pointsNames += l.getName();
		}
		MatchData data = RobotUtils.getRobotAttribute(MatchData.class,
				servicesProvider);
		data.put(PROPERTY_TRAJECTORY, pointsNames);

		buildControlPoints();

		computeNextRequests();
	}

	private void sendNextRequest() {
		if (navigationSuspended) {
			return;
		}
		if (requests != null && nextRequest < requests.size()) {
			LOGGER.finest("sending next request");
			RobotDeviceRequest r = requests.get(nextRequest++);
			queue.offer(r);
		} else {
			nextRequest = 0;
			requests = null;
			computeNextRequests();
			sendNextRequest();
		}
	}

	public void setAsynchronous(boolean asynchronous) {
		this.asynchronous = asynchronous;
	}

	private void setCurrentCornCollected() {
		if (currentCorn != null) {
			cornsCollected++;
			currentCorn.setAvailable(false);
			LOGGER.fine("corn collected " + currentCorn);
		}
	}

	public void setProperties(Properties properties) {
		this.properties = properties;

		// Asynchronous trajectory
		String s = properties.getProperty(PROPERTY_ASYNCHRONOUS,
				PROPERTY_ASYNCHRONOUS_DEFAULT_VALUE);
		asynchronous = Boolean.valueOf(s);
		LOGGER.fine("asynchronous: " + asynchronous);
	}

	public void setServicesProvider(IRobotServiceProvider provider) {
		this.servicesProvider = provider;
	}

	public void shutdown() {
		terminated = true;
		if (sender != null) {
			sender.interrupt();
		}
		sender = null;
		IRobotDevicesHandler handler = servicesProvider
				.getService(IRobotDevicesHandler.class);
		handler.removeDeviceListener(this);
		handler.getRequestDispatcher().purgeQueue();
		IComService comService = servicesProvider.getService(IComService.class);
		comService.writeOutData(new StopOutData());
		comService.writeOutData(new CollectTomato2010OutData(Action.OFF));

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void startAsynchronousTrajectory() {
		LOGGER.fine("starting asynchronous trajectory");

		// MatchData data = RobotUtils.getRobotAttribute(MatchData.class,
		// servicesProvider);
		// requests = (List<RobotDeviceRequest>) data.get(PROPERTY_REQUESTS);
		// nextRequest = 0;

		IRobotDevicesHandler handler = servicesProvider
				.getService(IRobotDevicesHandler.class);
		final DeviceRequestDispatcher dispatcher = handler
				.getRequestDispatcher();

		// Mode asynchrone
		NavigationDevice device = (NavigationDevice) handler
				.getDevice(NavigationDevice.NAME);
		device.setAsynchronous(true);

		sender = new Thread(new Runnable() {
			@Override
			public void run() {
				dispatcher.sendRequest(new NavigationInitializeRequest());
				while (!terminated) {
					try {
						RobotDeviceRequest r = queue.take();
						dispatcher.sendRequest(r);
						handleSentRequest(r);
					} catch (InterruptedException e) {
						LOGGER.finest("sending thread interrupted");
						e.printStackTrace();
					}
				}
			}
		}, "AsynchronousTrajectoryThread");
		sender.start();
		sendNextRequest();
	}

	private void startSynchronousTrajectory() {
		LOGGER.fine("starting synchronous trajectory");

		// MatchData data = RobotUtils.getRobotAttribute(MatchData.class,
		// servicesProvider);
		// final List<RobotDeviceRequest> requests = (List<RobotDeviceRequest>)
		// data.get(PROPERTY_REQUESTS);

		IRobotDevicesHandler handler = servicesProvider
				.getService(IRobotDevicesHandler.class);
		final DeviceRequestDispatcher dispatcher = handler
				.getRequestDispatcher();

		// Mode synchrone
		NavigationDevice device = (NavigationDevice) handler
				.getDevice(NavigationDevice.NAME);
		device.setAsynchronous(false);

		sender = new Thread(new Runnable() {
			@Override
			public void run() {
				for (RobotDeviceRequest r : requests) {
					if (r instanceof NavigationRequest
							&& !(r instanceof StopRequest)) {
						dispatcher.sendRequest(new StopRequest());
					}
					dispatcher.sendRequest(r);

					if (terminated) {
						break;
					}
				}
			}
		}, "SynchronousTrajectoryThread");
		sender.start();
	}

	public void startTrajectory() {
		if (asynchronous) {
			startAsynchronousTrajectory();
		} else {
			startSynchronousTrajectory();
		}
	}

	private void suspendNavigation() {
		navigationSuspended = true;
	}

	private void trajectoryDone() {
		if (!goingToGoal) {
			// on va au but
			goToGoal();
		} else {
			if (!goalTrajectory) {
				// on est au but
				// on attend la dépose
				goalTrajectory = true;
				List<RobotDeviceRequest> request = new ArrayList<RobotDeviceRequest>();
				request.add(new Specific2010SleepRequest(DURATION_RELEASE));
				request.add(new MoveRequest(-DISTANCE_GOAL_BACKWARD));
				nextRequest = 0;
				this.requests = request;
			} else {
				goalTrajectory = false;
				// on sélectionne une nouvelle trajectoire
				cornsCollected = 0;
				tomatoesCollected = 0;
				selectLocationGroup();
			}
		}
	}

	private boolean waitForResult(RobotDeviceRequest r) {
		if (r instanceof CollectTomato2010Request
				|| r instanceof Specific2010SleepRequest
				|| r instanceof WaitPositionUpdateRequest) {
			return false;
		}
		return true;
	}
}
