package org.cen.cup.cup2011.robot.match;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import org.cen.actions.IGameAction;
import org.cen.actions.TrajectoryData;
import org.cen.cup.cup2011.actions.GameActionMap2011;
import org.cen.cup.cup2011.actions.GameActionService2011;
import org.cen.cup.cup2011.device.gripper2011.GetKingPresenceRequest2011;
import org.cen.cup.cup2011.device.gripper2011.Gripper2011Handler;
import org.cen.cup.cup2011.device.gripper2011.GripperCloseNoDelayRequest2011;
import org.cen.cup.cup2011.device.gripper2011.GripperCloseRequest2011;
import org.cen.cup.cup2011.device.gripper2011.GripperRequest2011;
import org.cen.cup.cup2011.device.gripper2011.GripperUpRequest2011;
import org.cen.cup.cup2011.device.gripper2011.KingPresenceEvent;
import org.cen.cup.cup2011.device.gripper2011.PawnDropRequest2011;
import org.cen.cup.cup2011.device.gripper2011.PawnPickUpRequest2011;
import org.cen.cup.cup2011.device.vision2011.LookForPawnRequest;
import org.cen.cup.cup2011.device.vision2011.Vision2011InitializeRequest;
import org.cen.cup.cup2011.gameboard.GameBoard2011;
import org.cen.cup.cup2011.gameboard.configuration.GameboardConfigurationAnalyzer;
import org.cen.cup.cup2011.gameboard.configuration.GameboardConfigurationHandler2011;
import org.cen.cup.cup2011.gameboard.configuration.PawnConfiguration;
import org.cen.cup.cup2011.gameboard.configuration.PawnPosition;
import org.cen.cup.cup2011.navigation.DeadZoneHandler2011;
import org.cen.cup.cup2011.navigation.NavigationHandler2011;
import org.cen.geom.Point2D;
import org.cen.geom.Point2D.Double;
import org.cen.logging.LoggingUtils;
import org.cen.navigation.AbstractDeadZoneHandler;
import org.cen.navigation.INavigationMap;
import org.cen.navigation.ITrajectoryService;
import org.cen.navigation.Location;
import org.cen.robot.attributes.impl.RobotPosition;
import org.cen.robot.brain.CollisionHandler;
import org.cen.robot.brain.TimeHandler;
import org.cen.robot.configuration.IRobotConfiguration;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.configuration.ConfigurationReadRequest;
import org.cen.robot.device.lcd.LcdWriteRequest;
import org.cen.robot.device.navigation.MoveRequest;
import org.cen.robot.device.navigation.RotationOneWheelRequest;
import org.cen.robot.device.navigation.RotationRequest;
import org.cen.robot.device.navigation.SetInitialPositionRequest;
import org.cen.robot.device.navigation.StopRequest;
import org.cen.robot.device.request.IRobotDeviceRequest;
import org.cen.robot.device.timer.MatchFinishedRequest;
import org.cen.robot.device.timer.SleepRequest;
import org.cen.robot.match.AbstractMatchStrategyHandler;
import org.cen.robot.match.IMatchEvent;
import org.cen.robot.match.MatchData;
import org.cen.robot.match.MatchSide;
import org.cen.robot.match.events.CollisionDetectionEvent;
import org.cen.robot.match.events.MatchConfigurationDone;
import org.cen.robot.match.events.MatchFinishedEvent;
import org.cen.robot.match.events.MatchStartedEvent;
import org.cen.robot.match.events.MoveStoppedEvent;
import org.cen.robot.match.events.OpponentMovedEvent;
import org.cen.robot.match.events.PositionReachedEvent;
import org.cen.robot.match.events.RobotInitializedEvent;
import org.cen.robot.match.events.TimerEvent;
import org.cen.robot.utils.RobotUtils;
import org.cen.util.StringConstants;
import org.cen.vision.IVisionService;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * Implementation of the strategy handler for the cup 2011.
 * 
 * @author Emmanuel ZURMELY
 */
public class StrategyHandler2011 extends AbstractMatchStrategyHandler implements ResourceLoaderAware {
    private static final String ACTION_PICKUP_ANALYZE = "A";

    private static final String ACTION_NEUTRAL = "N";

    private static final String ACTION_PICKUP = "T";

    private static final String ACTION_DROP = "D";

    private static final String ACTION_DROP_KING = "K";

    private static final double DISTANCE_OBSTACLE = 400d;

    public static final String PROPERTY_INITIAL_POSITION = "initialPosition";

    private static final Logger LOGGER = LoggingUtils.getClassLogger();

    private static final double DISTANCE_OPPONENT_FROM_PAWN = 500;

    private static final double PROBABILITY_DECREASE_PAWN_PRESENCE = .9d;

    private static final double TARGET_MIN_PRESENCE_PROBABILITY = .3d;

    // Distance minimum d'une cible à un autre pion
    private static final double DISTANCE_TARGET_FROM_PAWNS = 260d;

    private boolean pickerEnabled = true; // activation de la pince

    private boolean visionEnabled = false; // pas de vision

    private final List<List<CheckPoint>> sequences = new ArrayList<List<CheckPoint>>();

    private int lastSequenceIndex;

    private int sequenceIndex;

    private int currentSequence;

    // en remplacement du navigation device, fournit des fausses positions
    private static final Boolean FAKE_VISION = false;

    // gestion des collisions par les balises
    private static final boolean BEACON = false;

    private static final double ANGLE_START = Math.toRadians(22.5);

    // Distance minimum d'une cible au robot
    private static final double DISTANCE_TARGET_FROM_ROBOT = 350d;

    private FakeVisionHandler2011 fakeVision;

    private CollisionHandler collisionHandler;

    private StrategyHandler2011Context fsm;

    private ConfigurationHandler2011 configurationHandler;

    private TimeHandler timeHandler;

    private NavigationHandler2011 navigationHandler;

    private ResourceLoader resourceLoader;

    private String resourcesPath;

    private AbstractDeadZoneHandler deadZoneHandler;

    private GameboardConfigurationAnalyzer gameBoardConfigurationAnalyzer;

    private GameboardConfigurationHandler2011 gameBoardConfigurationHandler;

    private Vision2011Handler vision2011Handler;

    private List<Location> currentPath;

    private StrategyStep currentStep;

    private MatchSide matchSide;

    private GameActionMap2011 gameActionMap;

    private PawnPosition targetPawn;

    private int currentRequestIndex;

    private boolean pickUp;

    private List<IRobotDeviceRequest> requests;

    private PawnPosition targetDropPosition;

    private GameActionService2011 gameActionService;

    private TrajectoryData trajectoryData;

    private List<IRobotDeviceRequest> moveToAnalyzeRequests;

    private StrategyStep nextStep;

    private boolean moving;

    // TODO à enlever et à gérer mieux
    private boolean nextPosition = false;

    private boolean startReceived = false;

    private String debugPath = null;

    private boolean initialLookup = false;

    private boolean analysisDuringMatch = true;

    private int startSequenceNumber;

    private Gripper2011Handler gripper2011Handler;

    private boolean pawnAvailableForBuild = false;

    private boolean pickUpSequence = true;

    private int targetIndex = 0;

    private int dropIndex = 0;

    private boolean isKing = false;

    private void addNextBuildSequence() {
        String[] target = null;
        if (pickUpSequence) {
            target = getNextTargetSequence();
        } else {
            target = getNextDropSequence();
        }

        pickUpSequence = !pickUpSequence;

        if (target == null) {
            return;
        }

        LOGGER.finest("next target: " + target);

        ArrayList<CheckPoint> sequence = new ArrayList<CheckPoint>();
        initSequence(target, sequence);
        sequences.add(sequence);
    }

    private void buildNextRequests() {
        clearRequests();
        requests = gameActionService.buildNextRequests(trajectoryData, gameActionMap);

        if (requests.isEmpty()) {
            // fin de la trajectoire
            clearTrajectory();
            if (currentPath.size() == 1) {
                // cherche une autre position
                nextPosition = true;
                clearRequests();
            }
        }
    }

    private void buildTrajectory() {
        IGameAction action = null;
        currentPath = null;

        if (!visionEnabled) {
            action = nextCheckPoint();
        } else {
            switch (currentStep) {
            case DROP_TARGET:
                buildTrajectoryToDropPosition();
                action = gameActionMap.getPawnDropAction();
                break;
            case FIND_TARGET:
                buildTrajectoryToPawn();
                action = gameActionMap.getPawnPickUpAction();
                break;
            case MOVE_TO_ANALYZE:
                if (moveToAnalyzeRequests == null) {
                    moveToAnalyzeRequests = new ArrayList<IRobotDeviceRequest>();
                    moveToAnalyzeRequests.add(new RotationRequest(Math.PI / 4));
                    moveToAnalyzeRequests.add(new SleepRequest(3000));
                }
                clearRequests();
                requests = moveToAnalyzeRequests;
                return;
            }
        }

        // Initialisation du calcul de trajectoire
        clearTrajectory();
        if (currentPath != null && currentPath.size() > 0) {
            logPath();
            Location end = currentPath.get(currentPath.size() - 1);
            RobotPosition position = RobotUtils.getRobotAttribute(RobotPosition.class, servicesProvider);
            // on démarre la trajectoire à l'index 1, il n'est pas utile de
            // rejoindre le point à l'index 0
            trajectoryData = new TrajectoryData(currentPath, 1, position.getCentralPoint(), end.getPosition(),
                    position.getAlpha(), action);
        }
    }

    private void buildTrajectoryToDropPosition() {
        RobotPosition robotPosition = RobotUtils.getRobotAttribute(RobotPosition.class, servicesProvider);
        Point2D position = robotPosition.getCentralPoint();
        targetDropPosition = getDropPosition(position);
        if (targetDropPosition == null) {
            // on ne trouve pas de position de dépose
            currentPath = null;
            LOGGER.fine("No drop position found");
        } else {
            LOGGER.finest("Drop position: " + targetDropPosition);
            Point2D destination = targetDropPosition.getPosition();
            ITrajectoryService trajectory = servicesProvider.getService(ITrajectoryService.class);
            List<Location> path = trajectory.getPath(position, destination);
            currentPath = path;
        }
    }

    private void buildTrajectoryToPawn() {
        RobotPosition robotPosition = RobotUtils.getRobotAttribute(RobotPosition.class, servicesProvider);
        Point2D position = robotPosition.getCentralPoint();
        targetPawn = getTargetPawn(position);
        if (targetPawn == null) {
            // on ne trouve pas de pion
            currentPath = null;
            LOGGER.fine("No pawn to pick up was found");
        } else {
            LOGGER.finest("Target pawn: " + targetPawn);
            Point2D destination = targetPawn.getPosition();
            ITrajectoryService trajectory = servicesProvider.getService(ITrajectoryService.class);
            List<Location> path = trajectory.getPath(position, destination);
            currentPath = path;
        }
    }

    private void clearRequests() {
        requests = null;
        currentRequestIndex = 0;
    }

    private void clearTrajectory() {
        trajectoryData = null;
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

        configurationHandler = new ConfigurationHandler2011(servicesProvider);
        deadZoneHandler = new DeadZoneHandler2011(servicesProvider);
        if (BEACON) {
            collisionHandler = new CollisionHandler(servicesProvider, deadZoneHandler);
        }
        navigationHandler = new NavigationHandler2011(servicesProvider);
        gameActionMap = new GameActionMap2011(servicesProvider);
        gameBoardConfigurationHandler = new GameboardConfigurationHandler2011(servicesProvider);
        gameBoardConfigurationHandler.setAdjustPawnPositions(true);
        gameBoardConfigurationAnalyzer = new GameboardConfigurationAnalyzer(servicesProvider,
                gameBoardConfigurationHandler.getInitialPawnPositions(), debugPath);
        vision2011Handler = new Vision2011Handler(servicesProvider, gameBoardConfigurationHandler, this);
        gripper2011Handler = new Gripper2011Handler(servicesProvider);
        fakeVision = new FakeVisionHandler2011(vision2011Handler);
        gameActionService = new GameActionService2011(servicesProvider);

        IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);

        // Demande de configuration
        LOGGER.fine("Waiting for match configuration");
        handler.sendRequest(new ConfigurationReadRequest());

        writeLCD("PC Connected.");
    }

    public void doHandleCollision() {
        // inutile de stopper si le robot ne se déplace pas
        if (!moving) {
            return;
        }

        LOGGER.fine("Avoiding collision");

        // recommence l'étape en cours
        nextStep = currentStep;
        LOGGER.fine("Restarting step: " + nextStep.name());
    }

    public void doStartTrajectory() {
        LOGGER.fine("Starting trajectory");
        deadZoneHandler.setEnabled(true);
        gameBoardConfigurationAnalyzer.interrupt();
        startTrajectory();
    }

    public void doStopTrajectory() {
        LOGGER.fine("Stopping trajectory");
        sendRequest(new StopRequest());
        sendRequest(new MatchFinishedRequest());
        stop();
    }

    public void doWaitForMatchStart() {
        LOGGER.fine("Waiting for match start");
        if (!visionEnabled) {
            // initialisation des séquences de déplacement
            initSequences();
        }
        if (visionEnabled) {
            initialLookForPawns();
        }
        if (startReceived) {
            fsm.MatchStarted();
        }
    }

    public void doWaitForRobotInitialization() {
        LOGGER.fine("Waiting for initialization");
        timeHandler = new TimeHandler(servicesProvider);
    }

    private void firstMove() {
        double startAngle = ANGLE_START;
        if (matchSide == MatchSide.RED) {
            startAngle = -startAngle;
        }
        requests = new ArrayList<IRobotDeviceRequest>();
        requests.add(new RotationOneWheelRequest(startAngle));
        requests.add(new GripperCloseNoDelayRequest2011());
    }

    private CheckPoint getCheckPoint() {
        lastSequenceIndex = sequenceIndex;
        List<CheckPoint> checkPoints = sequences.get(currentSequence);
        CheckPoint checkPoint = checkPoints.get(sequenceIndex++);
        return checkPoint;
    }

    private PawnPosition getDropPosition(Point2D robotPosition) {
        List<PawnPosition> dropPositions = gameBoardConfigurationHandler.getDropPositions();
        // Tri les positions en distance par rapport au robot
        Collections.sort(dropPositions, new PawnDistanceComparator(robotPosition));

        // récupérer une case dans l'axe du robot si possible sinon on passe
        // sur la boucle
        // TODO à affiner
        double robotOrientation = Math.atan2(robotPosition.getY(), robotPosition.getX());
        double dx, dy;
        for (PawnPosition dropPosition : dropPositions) {
            if (isValidDropPosition(dropPosition)) {
                dx = dropPosition.getPosition().getX() - robotPosition.getX();
                dy = dropPosition.getPosition().getY() - robotPosition.getY();
                double relativeDropPositionOrientation = Math.atan2(dy, dx);
                if (robotOrientation > relativeDropPositionOrientation * 0.9d
                        && robotOrientation < relativeDropPositionOrientation * 1.1d
                        && robotPosition.distance(dropPosition.getPosition()) < 520) {
                    // on passe la première position valide
                    if (nextPosition) {
                        nextPosition = false;
                    } else {
                        return dropPosition;
                    }
                }
            }
        }

        for (PawnPosition dropPosition : dropPositions) {
            if (isValidDropPosition(dropPosition)) {
                // on passe la première position valide
                if (nextPosition) {
                    nextPosition = false;
                    continue;
                } else {
                    return dropPosition;
                }
            }
        }
        return null;
    }

    private String[] getNextDropSequence() {
        if (targetIndex == 5) {
            dropIndex = 5;
        }
        switch (dropIndex) {
        case 1:
            return new String[] { "A5D", "E5N" };
        case 2:
            return new String[] { "C3D" };
        case 3:
            return new String[] { "E5D" };
        case 4:
            return new String[] { "G3D" };
        case 5:
            MatchData2011 data = (MatchData2011) RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);
            if (data.getBonus()) {
                return new String[] { "K5D" };
            } else {
                return new String[] { "K3D" };
            }
        }
        return null;
    }

    private String[] getNextTargetSequence() {
        targetIndex++;
        switch (targetIndex) {
        case 1:
            return new String[] { "GZR1A", "D5N" };
        case 2:
            return new String[] { "GZR2A", "E3N" };
        case 3:
            return new String[] { "GZR3A", "G5N" };
        case 4:
            return new String[] { "GZR4A", "I3N" };
        case 5:
            return new String[] { "GZR5A", "I3N" };
        }
        return null;
    }

    private Point2D getPointFromRobot(double distance) {
        RobotPosition position = RobotUtils.getRobotAttribute(RobotPosition.class, servicesProvider);
        Point2D p = position.getCentralPoint();
        double x = p.getX();
        double y = p.getY();
        double angle = position.getAlpha();
        x += distance * Math.cos(angle);
        y += distance * Math.sin(angle);
        return new Point2D.Double(x, y);
    }

    private PawnPosition getTargetPawn(Point2D robotPosition) {
        List<PawnPosition> pawns = gameBoardConfigurationHandler.getPawnPositions();
        // Trie les pions en distance par rapport au robot
        Collections.sort(pawns, new PawnDistanceComparator(robotPosition));
        // Recherche le premier pion pouvant être une cible valide
        for (PawnPosition pawn : pawns) {
            if (isValidTargetPawn(pawn, robotPosition)) {
                if (nextPosition) {
                    nextPosition = false;
                } else {
                    return pawn;
                }
            }
        }
        return null;
    }

    private void handleCollision(CollisionDetectionEvent event) {
        fsm.CollisionDetected();
    }

    @Override
    public boolean handleEvent(IMatchEvent event) {
        if (event instanceof RobotInitializedEvent) {
            LOGGER.fine("Robot initialized");
            startReceived = false;
            fsm.RobotInitializationDone();
        } else if (event instanceof MatchConfigurationDone) {
            LOGGER.fine("Configuration done");
            setMatchSide();
            setVision();
            fsm.ConfigurationDone();
        } else if (event instanceof CollisionDetectionEvent) {
            LOGGER.fine("Collision event");
            handleCollision((CollisionDetectionEvent) event);
        } else if (event instanceof OpponentMovedEvent) {
            handleOpponentMoved((OpponentMovedEvent) event);
        } else if (event instanceof MatchStartedEvent) {
            LOGGER.fine("Match started");
            startReceived = true;
            fsm.MatchStarted();
        } else if (event instanceof MatchFinishedEvent) {
            LOGGER.fine("Match stopped");
            fsm.MatchStopped();
        } else if (event instanceof PositionReachedEvent) {
            nextRequest();
        } else if (event instanceof MoveStoppedEvent) {
            LOGGER.fine("Move stopped");
            if (!visionEnabled) {
                sequenceIndex = lastSequenceIndex;
            }
            clearTrajectory();
            clearRequests();
            setObstacle();
        } else if (event instanceof TimerEvent) {
            nextRequest();
        } else if (event instanceof GameboardAnalysisDoneEvent) {
            handleGameboardAnalysisDone();
        } else if (event instanceof KingPresenceEvent) {
            handleKing((KingPresenceEvent) event);
        }
        return true;
    }

    private void handleGameboardAnalysisDone() {
        // nouvelle analyse
        if (initialLookup) {
            initialLookForPawns();
        }
    }

    private void handleKing(KingPresenceEvent event) {
        isKing = event.isKingPresent();
        if (!pawnAvailableForBuild || !isKing) {
            dropIndex++;
        } else {
            sendRequest(new GripperUpRequest2011());
        }
        pawnAvailableForBuild = !isKing;
        nextRequest();
    }

    private void handleOpponentMoved(OpponentMovedEvent event) {
        Point2D position = event.getPosition();
        List<PawnPosition> pawns = gameBoardConfigurationHandler.getNearestPawnPositions(position,
                DISTANCE_OPPONENT_FROM_PAWN);
        // prend en compte les pions bien positionnés
        pawns.addAll(gameBoardConfigurationHandler.getNearestDropPositions(position, DISTANCE_OPPONENT_FROM_PAWN));
        for (PawnPosition pawn : pawns) {
            pawn.adjustPresenceProbability(PROBABILITY_DECREASE_PAWN_PRESENCE, 0d);
        }
        gameBoardConfigurationHandler.updateGameBoardView();
    }

    private void initialLookForPawns() {
        initialLookup = true;
        // vide les pions déjà détectés et relance une détection
        gameBoardConfigurationHandler.clearPawns();
        lookForPawns(true);
    }

    /**
     * 
     * @param sequenceTab
     * @param sequence
     */
    private void initSequence(String[] sequenceTab, ArrayList<CheckPoint> sequence) {
        ITrajectoryService trajectory = servicesProvider.getService(ITrajectoryService.class);
        INavigationMap map = trajectory.getNavigationMap();
        Map<String, Location> locations = map.getLocationsMap();

        for (String checkPoint : sequenceTab) {
            String name = checkPoint.substring(0, checkPoint.length() - 1);
            String action = checkPoint.substring(checkPoint.length() - 1, checkPoint.length());
            if (action.equals(ACTION_DROP) && isKing) {
                action = ACTION_DROP_KING;
            }

            Location l = locations.get(name);
            if (l == null) {
                System.err.println("invalid location name: " + name);
                return;
            }
            Point2D p = l.getPosition();
            int x = (int) p.getX();
            int y = (int) p.getY();
            if (matchSide.equals(MatchSide.RED)) {
                y = GameBoard2011.BOARD_HEIGHT - y;
            }
            Double position = new Point2D.Double(x, y);
            System.out.println("Checkpoint: " + checkPoint + " at " + position + ", action: " + action);
            sequence.add(new CheckPoint(position, action));
        }
    }

    /**
     * initialisation of each moves sequences for the blind robot
     */
    private void initSequences() {
        sequences.clear();

        ArrayList<CheckPoint> sequenceInit = new ArrayList<CheckPoint>();
        sequences.add(sequenceInit);
        if (startSequenceNumber == 1) {
            // Sequence de début, tente de prendre le pion central
            String[] sequenceInitTab = { "A10N", "A9N", "E9N", "G7D", "C5N" };
            initSequence(sequenceInitTab, sequenceInit);
        } else if (startSequenceNumber == 2) {
            MatchData2011 data = (MatchData2011) RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);
            if (data.getBuild()) {
                pawnAvailableForBuild = false;
                pickUpSequence = true;
                addNextBuildSequence();
            } else if (data.getBonus()) {
                // Sequence de prise des pions de la zone verte avec bonus
                String[] sequenceInitTab = { "A3N", "GZR1T", "D5N", "A5D", "GZR2T", "E3N", "C3D", "G3N", "GZR3T",
                        "G5N", "E5D", "I3N", "GZR4T", "I3N", "G3D", "I3N", "GZR5T", "I3N", "I7N", "K7D" };
                initSequence(sequenceInitTab, sequenceInit);
            } else {
                // Sequence de prise des pions de la zone verte
                String[] sequenceInitTab = { "A3N", "GZR1T", "D5N", "A5D", "GZR2T", "E3N", "C3D", "G3N", "GZR3T",
                        "G5N", "E5D", "I3N", "GZR4T", "I3N", "G3D", "I3N", "GZR5T", "I3N", "K3D", "J3N", "I3N" };
                initSequence(sequenceInitTab, sequenceInit);
            }
        }

        // // Sequence de début, tente de prendre le pion central
        // String[] sequenceInitTab = { "A9N", "F8T", "J8N", "K7D" };
        // ArrayList<CheckPoint> sequenceInit = new ArrayList<CheckPoint>();
        // sequences.add(sequenceInit);
        // initSequence(sequenceInitTab, sequenceInit);
        //
        // // sequence 1 : tente de positionner de pions encore jamais placés
        // String[] sequence1Tab = { "H6T", "I5D", "H4T", "G3D", "F6T", "G7D",
        // "F4T", "E5D", "D4T", "C3D", "D6T", "C7D", "B4T", "A5D", "B6T", "A9D"
        // };
        // ArrayList<CheckPoint> sequence1 = new ArrayList<CheckPoint>();
        // sequences.add(sequence1);
        // initSequence(sequence1Tab, sequence1);
        //
        // // sequence 2 : tente de positionner de pions déjà placés
        // String[] sequence2Tab = { "A11T", "A9D", "C13T", "E13D", "E11T",
        // "C11D", "G13T", "I13D", "I11T", "G11D", "C9T", "I9D", "I7T", "G7D",
        // "G5T", "I5D", "G5N", "I3T", "G3D", "E3T", "E5D", "C5T", "E5D", "A3T",
        // "A5D", "E7T", "C7D", "C9T", "E9D", "A7T", "A5D", "C9N" };
        // ArrayList<CheckPoint> sequence2 = new ArrayList<CheckPoint>();
        // sequences.add(sequence2);
        // initSequence(sequence2Tab, sequence2);
    }

    private boolean isOnBorders(double distance) {
        Point2D p = getPointFromRobot(distance + DISTANCE_OBSTACLE);
        double x = p.getX();
        double y = p.getY();
        return (x < 0 || x > GameBoard2011.BOARD_WIDTH || y < 0 || y > GameBoard2011.BOARD_HEIGHT);
    }

    private boolean isValidDropPosition(PawnPosition dropPosition) {
        Set<PawnConfiguration> c = dropPosition.getConfiguration();
        // La position de dépose ne doit pas comporter de pion
        EnumSet<PawnConfiguration> s = EnumSet.of(PawnConfiguration.SINGLE, PawnConfiguration.DOUBLE,
                PawnConfiguration.KING, PawnConfiguration.QUEEN);
        s.retainAll(c);
        return s.isEmpty();
    }

    private boolean isValidTargetPawn(PawnPosition pawn, Point2D robotPosition) {
        // TODO : ajouter la prise en compte de la position dans la case
        MatchSide side = gameBoardConfigurationHandler.getPositionSide(pawn.getPosition());
        boolean notOwned = side != matchSide;
        // le pion doit être suffisament éloigné du pion déposé
        boolean notNearDroppedPawn;
        if (targetDropPosition == null) {
            notNearDroppedPawn = true;
        } else {
            notNearDroppedPawn = pawn.getPosition().distance(targetDropPosition.getPosition()) > DISTANCE_TARGET_FROM_ROBOT;
        }
        // si le pion est trop proche d'un autre on passe au suivant
        List<PawnPosition> nearestPawnPosition = gameBoardConfigurationHandler.getNearestPawnPositions(
                pawn.getPosition(), DISTANCE_TARGET_FROM_PAWNS);
        return notOwned && notNearDroppedPawn && (pawn.getPresenceProbability() > TARGET_MIN_PRESENCE_PROBABILITY)
                && nearestPawnPosition.size() == 1;
    }

    private void logPath() {
        StringBuilder sb = new StringBuilder();
        for (Location l : currentPath) {
            sb.append(l.getName());
            sb.append('-');
        }
        sb.deleteCharAt(sb.length() - 1);
        LOGGER.finest(sb.toString());
    }

    private void lookForPawns() {
        lookForPawns(false);
    }

    private void lookForPawns(boolean initial) {
        if (!visionEnabled) {
            return;
        }

        if (FAKE_VISION) {
            fakeVision.visionRequest();
        } else {
            sendRequest(new LookForPawnRequest(initial));
        }
    }

    /**
     * compute the path to the nex checkPoint and get action wich is associated
     * 
     * @return
     */
    private IGameAction nextCheckPoint() {
        if (!nextSequence()) {
            return null;
        }

        RobotPosition robotPosition = RobotUtils.getRobotAttribute(RobotPosition.class, servicesProvider);
        Point2D position = robotPosition.getCentralPoint();
        CheckPoint checkPoint = getCheckPoint();
        if (robotPosition.getCentralPoint().distance(checkPoint.getPosition()) < 155) {
            checkPoint = getCheckPoint();
        }
        Point2D destination = checkPoint.getPosition();
        ITrajectoryService trajectory = servicesProvider.getService(ITrajectoryService.class);
        List<Location> path = trajectory.getPath(position, destination);
        currentPath = path;
        IGameAction action = null;
        if (checkPoint.getAction().equals(ACTION_DROP)) {
            action = gameActionMap.getPawnDropAction();
        } else if (checkPoint.getAction().equals(ACTION_DROP_KING)) {
            action = gameActionMap.getPawnDropKingAction();
        } else if (checkPoint.getAction().equals(ACTION_PICKUP)) {
            action = gameActionMap.getPawnPickUpAction();
        } else if (checkPoint.getAction().equals(ACTION_NEUTRAL)) {
            action = null;
        } else if (checkPoint.getAction().equals(ACTION_PICKUP_ANALYZE)) {
            action = gameActionMap.getPawnPickUpAndAnalyzeAction();
        }
        return action;
    }

    private void nextRequest() {
        // On essaye d'analyser la piste après chaque mouvement
        // TODO à tester
        if (analysisDuringMatch) {
            lookForPawns();
        }

        if (requests != null && currentRequestIndex < requests.size()) {
            // on a des requêtes en attente, on envoie la suivante
            sendRequest(requests.get(currentRequestIndex++));
        } else if (trajectoryData != null) {
            // on a plus de requêtes à traiter, on en construit de nouvelles
            buildNextRequests();
            // et on retente le traitement
            nextRequest();
            return;
        } else {
            // on a plus de requêtes à traiter et la trajectoire est
            // terminée, on passe à l'étape suivante
            nextStep();
            sendRequest(new SleepRequest(10));
            return;
        }
    }

    /**
     * go to the next sequence for the blind robot
     */
    private boolean nextSequence() {
        List<CheckPoint> checkPoints = sequences.get(currentSequence);
        if (sequenceIndex > checkPoints.size() - 1) {
            sequenceIndex = 0;
            currentSequence++;
        }
        if (currentSequence > sequences.size() - 1) {
            MatchData2011 matchData = (MatchData2011) RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);
            if (matchData.getBuild()) {
                addNextBuildSequence();
                if (currentSequence < sequences.size()) {
                    return true;
                }
            }
            // plus rien à exécuter
            nextStep = null;
            return false;
        }
        // séquence suivante
        return true;
    }

    private void nextStep() {
        if (nextStep == null) {
            return;
        }

        currentStep = nextStep;
        LOGGER.fine("current step: " + currentStep.name());
        switch (currentStep) {
        case START:
            firstMove();
            if (visionEnabled) {
                nextStep = StrategyStep.FIND_TARGET;
            } else {
                nextStep = StrategyStep.EXECUTE_SEQUENCE;
            }
            break;
        case FIND_TARGET:
            if (pickUp) {
                // si on a trouvé une cible, la prochaine étape est la dépose
                nextStep = StrategyStep.DROP_TARGET;
            } else {
                buildTrajectory();
                if (currentPath == null) {
                    // si on ne trouve pas de cible, on essaye de bouger pour
                    // effectuer une analyse
                    nextStep = StrategyStep.MOVE_TO_ANALYZE;
                } else {
                    // si on a trouvé une cible, la prochaine étape est la
                    // dépose
                    nextStep = StrategyStep.DROP_TARGET;
                }
            }
            break;
        case DROP_TARGET:
            if (!pickUp) {
                nextStep = StrategyStep.FIND_TARGET;
            } else {
                buildTrajectory();
                nextStep = StrategyStep.FIND_TARGET;
            }
            break;
        case MOVE_TO_ANALYZE:
            buildTrajectory();
            // l'acquisition se fait lors des mouvements, la prochaine étape
            // doit donc permettre de trouver une cible
            nextStep = StrategyStep.FIND_TARGET;
            break;
        case EXECUTE_SEQUENCE:
            buildTrajectory();
            nextStep = StrategyStep.EXECUTE_SEQUENCE;
            break;
        }
    }

    private void pawnDrop() {
        if (pickUp) {
            if (visionEnabled) {
                lookForPawns();
                gameBoardConfigurationHandler.handlePawnDroped(targetDropPosition);
            }
            pickUp = false;
            nextRequest();
        }
    }

    private void pawnPickUp() {
        if (!pickUp) {
            if (visionEnabled) {
                lookForPawns();
                gameBoardConfigurationHandler.handlePawnTaken(targetPawn);
            }
            pickUp = true;
            nextRequest();
        }
    }

    private void sendRequest(IRobotDeviceRequest request) {
        IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
        // on considère être en déplacement uniquement si on avance, pas si on
        // tourne ou recule
        moving = (request instanceof MoveRequest) && ((MoveRequest) request).getDistance() > 0;

        if (moving && !isOnBorders(((MoveRequest) request).getDistance())) {
            // handler.sendRequest(new EnableCollisionDetectionRequest(true));
            handler.sendRequest(request);
            return;
        } else if (request instanceof MoveRequest || request instanceof RotationRequest
                || request instanceof RotationOneWheelRequest) {
            // handler.sendRequest(new EnableCollisionDetectionRequest(false));
            handler.sendRequest(request);
            return;
        }

        if (request instanceof GripperCloseRequest2011 && timeHandler.getElapsedTime() < 5000) {
            return;
        }

        if (request instanceof GripperRequest2011) {
            if (request instanceof PawnPickUpRequest2011 && !pickUp) {
                if (pickerEnabled) {
                    handler.sendRequest(request);
                }
                pawnPickUp();
            } else if (request instanceof PawnDropRequest2011 && pickUp) {
                if (pickerEnabled) {
                    handler.sendRequest(request);
                }
                pawnDrop();
            } else if (request instanceof GetKingPresenceRequest2011) {
                if (pickerEnabled) {
                    handler.sendRequest(request);
                } else {
                    nextRequest();
                }
            } else {
                if (pickerEnabled) {
                    handler.sendRequest(request);
                }
                nextRequest();
            }
        } else {
            handler.sendRequest(request);
        }
    }

    public void setAnalysisDuringMatch(boolean value) {
        analysisDuringMatch = value;
    }

    public void setDebugPath(String path) {
        debugPath = path;
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
            s += "BLUE";
            break;
        case VIOLET:
            s += "RED";
            break;
        }
        writeLCD(s);
    }

    private void setMatchSide() {
        MatchData2011 data = (MatchData2011) RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);
        matchSide = data.getSide();
        gameBoardConfigurationHandler.setMatchSide(matchSide);
    }

    private void setObstacle() {
        // Obstacle position
        Point2D p = getPointFromRobot(DISTANCE_OBSTACLE);
        double x = p.getX();
        double y = p.getY();
        if (x > 0 && x < GameBoard2011.BOARD_WIDTH && y > GameBoard2011.GREEN_ZONE_HEIGHT
                && y < GameBoard2011.BOARD_HEIGHT - GameBoard2011.GREEN_ZONE_HEIGHT) {
            // Ne prend en compte les collisions que dans le damier
            deadZoneHandler.handleDeadZone(new Point2D.Double(x, y));
            sendRequest(new SleepRequest(1000));
        } else {
            nextRequest();
        }
    }

    public void setPickerEnabled(boolean value) {
        pickerEnabled = true;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void setResourcesPath(String resourcesPath) {
        this.resourcesPath = resourcesPath;
    }

    public void setSequenceNumber(int value) {
        startSequenceNumber = value;
    }

    private void setVision() {
        IVisionService vision = servicesProvider.getService(IVisionService.class);
        MatchData2011 data = (MatchData2011) RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);
        visionEnabled = !data.getHomologation() && (vision != null) && vision.isAvailable();

        // Initialisation de la vision
        if (visionEnabled) {
            sendRequest(new LcdWriteRequest("Vision ACTIVE"));
            IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
            handler.sendRequest(new Vision2011InitializeRequest(gameBoardConfigurationAnalyzer));
        } else {
            sendRequest(new LcdWriteRequest("Vision INACTIVE"));
        }
    }

    @Override
    public void start() {
        LOGGER.config("Starting match strategy");
        fsm = new StrategyHandler2011Context(this);
        LOGGER.config("Match strategy started");
        fsm.Start();
    }

    private void startTrajectory() {
        // on ajuste plus les positions détectées après le début du match
        gameBoardConfigurationHandler.setAdjustPawnPositions(false);
        lastSequenceIndex = 0;
        sequenceIndex = 0;
        currentSequence = 0;
        moving = false;
        pickUp = false;
        initialLookup = false;
        currentStep = StrategyStep.START;
        nextStep = StrategyStep.START;
        clearTrajectory();
        clearRequests();
        nextRequest();
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
        if (vision2011Handler != null) {
            vision2011Handler.shutdown();
            vision2011Handler = null;
        }
        if (gripper2011Handler != null) {
            gripper2011Handler.shutdown();
            gripper2011Handler = null;
        }
        if (gameBoardConfigurationAnalyzer != null) {
            gameBoardConfigurationAnalyzer.shutdown();
            gameBoardConfigurationAnalyzer = null;
        }
        if (fsm != null) {
            fsm = null;
        }
    }

    public void unhandled() {
        LOGGER.warning("Unhandled transition: " + fsm.getTransition());
    }

    private void writeLCD(String s) {
        sendRequest(new LcdWriteRequest(s));
    }
}
