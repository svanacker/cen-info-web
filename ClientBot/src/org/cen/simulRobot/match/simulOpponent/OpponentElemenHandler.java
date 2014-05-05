package org.cen.simulRobot.match.simulOpponent;

import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.cen.cup.cup2011.simulGameboard.elements.SimulPawnElement;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.TrajectoryCurve;
import org.cen.robot.TrajectoryCurve.Direction;
import org.cen.robot.device.navigation.position.com.PositionStatus;
import org.cen.robot.match.IMatchEvent;
import org.cen.robot.match.events.MatchStartedEvent;
import org.cen.simulRobot.match.ISimulMatchStrategy;
import org.cen.simulRobot.match.event.AMatchEvent;
import org.cen.simulRobot.match.event.MoveEvent;
import org.cen.simulRobot.match.event.StopEvent;
import org.cen.simulRobot.match.simulMoving.MovingHandlerListener;
import org.cen.simulRobot.match.simulMoving.event.SimulMovedEvent;
import org.cen.ui.gameboard.IGameBoardService;
import org.cen.ui.gameboard.ISimulGameBoard;
import org.cen.ui.gameboard.elements.IMovableElement;
import org.cen.ui.gameboard.elements.SimulOpponentElement;
import org.cen.ui.gameboard.elements.SimulRobotElement;

/**
 * Handler wich handle opponents strategy
 * 
 * @author Benouamer Omar
 * 
 */
public class OpponentElemenHandler implements MovingHandlerListener {

    private class PickerHandler extends Thread {

        private final SimulPawnElement pickedUpElement;

        public PickerHandler(SimulPawnElement simulPawnElement) {
            this.pickedUpElement = simulPawnElement;
        }

        @Override
        public void run() {
            pickedUpElement.setPickable(false);
            while (!pickUpTerminated) {
                pickedUpElement.setPosition(opponentElement.getPosition());
            }
            pickedUpElement.setPickable(true);
        }

    }

    private class RobotDetectionHandler extends Thread {
        private final boolean detectionTerminated;
        private final SimulRobotElement robotElement;

        public RobotDetectionHandler() {
            // this.robotElement = robotElement;
            this.robotElement = gameboard.getElements(SimulRobotElement.class).get(0);
            detectionTerminated = false;
        }

        @Override
        public void run() {
            while (!detectionTerminated) {
                Shape opponentShape = opponentElement.getRobotDetectionElement().getAbsoluteBounds();
                if (opponentShape.intersects(robotElement.getAbsoluteBounds().getBounds())) {
                    // System.out.println("ROBOT DETECTED");
                    clearMoveEvents();
                    notifyEvent(new StopEvent(NAME));
                    // handleEvent(new MatchStartedEvent());
                    // basicStrategy();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // private boolean terminated;

    public static final String NAME = "opponent";

    private int currentMoveEventIndex;

    OpponentStrategyStep currentStep = OpponentStrategyStep.FIND_TARGET;

    private final ISimulGameBoard gameboard;

    private final List<MoveEvent> moveEvents = new ArrayList<MoveEvent>();

    private final SimulOpponentElement opponentElement;

    PickerHandler pickerHandler;

    // private AMovableElement targetElement;

    private boolean pickUpTerminated = false;

    RobotDetectionHandler robotDetectionHandler;

    private final IRobotServiceProvider servicesProvider;

    private final double speed;

    private Point2D targetPosition;

    public OpponentElemenHandler(IRobotServiceProvider pServicesProvider) {
        this.servicesProvider = pServicesProvider;
        this.gameboard = (ISimulGameBoard) servicesProvider.getService(IGameBoardService.class);
        this.speed = 0.10;
        this.opponentElement = (gameboard.getElements(SimulOpponentElement.class)).get(0);
        ISimulGameBoard gameBoard = (ISimulGameBoard) pServicesProvider.getService(IGameBoardService.class);
        gameBoard.getMovingHandler().addDeviceListener(this);
    }

    private void action() {
        switch (currentStep) {
        case FIND_TARGET:
            pickDown();
            buildTrajectory();
            currentStep = OpponentStrategyStep.DROP_TARGET;
            break;
        case DROP_TARGET:
            // ISimulGameBoard gameBoard =
            // (ISimulGameBoard)servicesProvider.getService(IGameBoardService.class);
            pickUp();
            buildTrajectory();
            currentStep = OpponentStrategyStep.FIND_TARGET;

        }
    }

    private void basicStrategy() {
        nextMoveEvent();
    }

    private void buildMoveEvents() {

        double relativeDistance = getDistance(targetPosition, opponentElement);
        double relativeAlpha = getAlpha(targetPosition, opponentElement);
        Direction direction = Direction.LEFT;
        if (relativeAlpha < 0) {
            direction = Direction.RIGHT;
            relativeAlpha = -relativeAlpha;
        }
        TrajectoryCurve curve = new TrajectoryCurve(0, 0);
        curve.setResults(relativeAlpha, 0, 0, direction);
        moveEvents.add(new MoveEvent(curve, speed, NAME));
        curve = new TrajectoryCurve(0, 0);
        curve.setResults(0, 0, relativeDistance, Direction.LEFT);
        moveEvents.add(new MoveEvent(curve, speed, NAME));
        targetPosition = null;
    }

    private void buildTrajectory() {

        switch (currentStep) {
        case FIND_TARGET:
            this.targetPosition = getPawnPosition();
            break;
        case DROP_TARGET:
            this.targetPosition = getDropPosition();
        }
    }

    private void clearMoveEvents() {
        moveEvents.clear();
        currentMoveEventIndex = 0;
    }

    private void currentStrategy() {
        basicStrategy();
    }

    private double getAlpha(Point2D nextElement, SimulOpponentElement opponentElement) {
        double relativeX = nextElement.getX() - opponentElement.getPosition().getX();
        double relativeY = nextElement.getY() - opponentElement.getPosition().getY();
        double relativeAlpha = -(opponentElement.getOrientation() - Math.atan2(relativeY, relativeX));
        return relativeAlpha;
    }

    private double getDistance(Point2D nextElement, SimulOpponentElement opponentElement) {
        double relativeDistance = opponentElement.getPosition().distance(nextElement);
        return relativeDistance;
    }

    private Point2D getDropPosition() {
        int aX = (int) (150 + Math.random() * 1600);
        int aY = (int) (600 + Math.random() * 1800);
        return new Point2D.Double(aX, aY);
    }

    @Override
    public String getHandlerName() {
        return NAME;
    }

    private Point2D getPawnPosition() {
        List<IMovableElement> pieceElements = new ArrayList<IMovableElement>();
        pieceElements.addAll(gameboard.getElements(SimulPawnElement.class));
        int random = (int) (Math.random() * pieceElements.size());
        IMovableElement targetElement = pieceElements.get(random);
        return targetElement.getPosition();
    }

    public void handleEvent(IMatchEvent event) {
        if (event instanceof MatchStartedEvent) {
            // robotDetectionHandler = new RobotDetectionHandler();
            // robotDetectionHandler.start();
            clearMoveEvents();
            currentStrategy();
        }
    }

    private void nextMoveEvent() {

        if (moveEvents != null && currentMoveEventIndex < moveEvents.size() && !moveEvents.isEmpty()) {
            // on a des requ�tes en attente, on envoie la suivante
            notifyEvent(moveEvents.get(currentMoveEventIndex++));
        } else if (targetPosition != null) {
            // on a plus de requ�tes � traiter, on en construit de nouvelles
            buildMoveEvents();
            // et on retente le traitement
            nextMoveEvent();

            return;
        } else {
            clearMoveEvents();
            // on a plus de requ�tes � traiter et la trajectoire est
            // termin�e,
            // on passe � l'�tape suivante
            nextStep();
            nextMoveEvent();
            return;
        }
    }

    private void nextStep() {

        switch (currentStep) {
        case FIND_TARGET:
            action();
            // buildTrajectory();
            // currentStep = OpponentStrategyStep.DROP_TARGET;
            break;
        case DROP_TARGET:
            action();
            // buildTrajectory();
            // currentStep = OpponentStrategyStep.FIND_TARGET;
        }
    }

    public void notifyEvent(AMatchEvent pMoveEvent) {
        ISimulMatchStrategy simulStrategy = servicesProvider.getService(ISimulMatchStrategy.class);
        simulStrategy.notifyEvent(pMoveEvent);
    }

    @Override
    public void onMovingHandlerData(SimulMovedEvent event) {

        if (!(event).getStatus().equals(PositionStatus.MOVING)) {
            // clearMoveEvents();
            currentStrategy();
        }
    }

    private void pickDown() {
        pickUpTerminated = true;
    }

    private void pickUp() {
        List<SimulPawnElement> elements = gameboard.getElements(SimulPawnElement.class);
        for (SimulPawnElement simulPawnElement : elements) {
            if (simulPawnElement.getPosition().distance(opponentElement.getPosition()) < 100) {
                pickUpTerminated = false;
                pickerHandler = new PickerHandler(simulPawnElement);
                pickerHandler.start();
                break;
            }
        }
    }

    public void shutdown() {
        // terminated = true;
    }
}
