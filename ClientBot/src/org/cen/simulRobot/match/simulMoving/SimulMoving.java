package org.cen.simulRobot.match.simulMoving;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.List;

import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.RobotPosition;
import org.cen.robot.TrajectoryCurve;
import org.cen.robot.TrajectoryCurve.Direction;
import org.cen.robot.device.navigation.position.com.PositionStatus;
import org.cen.simulRobot.match.event.MoveEvent;
import org.cen.simulRobot.match.simulMoving.event.AbsolutePositionEvent;
import org.cen.ui.gameboard.IGameBoardElement;
import org.cen.ui.gameboard.IGameBoardService;
import org.cen.ui.gameboard.elements.IMovableElement;

/**
 * Simule le deplacement du robot et envoie la position absolue et l'angle du
 * robot
 * 
 * @author Benouamer Omar
 * 
 */
public class SimulMoving extends Thread {

    private final double alphaTotal;

    private final TrajectoryCurve curve;

    private final Direction direction;

    private final double distanceRefresh;

    private final double distanceTotal;

    private final List<IGameBoardElement> elements;

    private final Rectangle2D gamePlayBound;

    private final String handler;

    private final IMovableElement movedElement;

    private final ISimulMovingHandler movingHandler;

    private final double radius;

    private final IRobotServiceProvider servicesProvider;

    private double speed; // speed element in m/s

    private boolean terminated;

    private double time;

    private final double alphaRefresh;

    /**
     * constructor
     * 
     * @param pEvent
     * @param servicesProvider
     */
    public SimulMoving(MoveEvent pEvent, IRobotServiceProvider pServicesProvider, ISimulMovingHandler pMovingHandler) {
        super("SimulMoving" + pEvent.getHandler());
        this.servicesProvider = pServicesProvider;
        this.curve = pEvent.getCurve();
        this.speed = pEvent.getSpeed();
        this.time = curve.getDistance() / speed;
        this.radius = curve.getRadius();
        this.direction = curve.getDirection();
        this.distanceTotal = curve.getDistance();
        this.alphaTotal = curve.getAngle();
        this.handler = pEvent.getHandler();
        this.movedElement = pEvent.getMovableElement();
        this.movingHandler = pMovingHandler;
        this.distanceRefresh = 20;
        this.alphaRefresh = 0.1;
        IGameBoardService gameBoard = servicesProvider.getService(IGameBoardService.class);
        gamePlayBound = gameBoard.getGameplayBounds();
        elements = gameBoard.getElements();
        terminated = false;
    }

    private void firePosition(PositionStatus status) {
        double x = movedElement.getPosition().getX();
        double y = movedElement.getPosition().getY();
        double alpha = movedElement.getOrientation();
        // angle between -2Pi and 2Pi
        alpha %= 2 * Math.PI;
        // angle between -Pi and Pi
        if (alpha < -Math.PI) {
            alpha += 2 * Math.PI;
        } else if (alpha > Math.PI) {
            alpha -= 2 * Math.PI;
        }

        movingHandler.onSimulMoveEvent(new AbsolutePositionEvent(x, y, alpha, status, handler));
    }

    /**
     * donne le temps estimé pour parcourir le trajet
     * 
     * @param adistance
     *            mm
     * @return time (mms-1)
     */
    // TODO ameliorer l'estimation du temps de deplacement
    private long getEstimatedTime(double adistance) {
        // return 1000 + Math.abs((int) (adistance / speed) * 32);
        return 100 + Math.abs((long) (adistance / speed));
    }

    private double getEstimatedTime(TrajectoryCurve pCurve) {
        double aDistance = pCurve.getDistance();
        double aAngle = pCurve.getAngle();
        double wheelsDistance = 230;
        int epsilonTime = 100;

        // tester si distance vaut 0
        if (aDistance == 0 && aAngle != 0) {
            return epsilonTime + Math.abs((long) ((aAngle * wheelsDistance / 2) / speed));
        }
        return getEstimatedTime(aDistance);
    }

    public String getHandler() {
        return handler;
    }

    public boolean isTerminated() {
        return terminated;
    }

    @Override
    public void run() {

        double sleepTime = getEstimatedTime(distanceRefresh);

        double totalTime = getEstimatedTime(curve);

        TrajectoryCurve remainingCurve = new TrajectoryCurve(curve.getLeftWheel(), curve.getRightWheel());
        remainingCurve.setResults(curve.getAngle(), radius, curve.getDistance(), direction);
        double remainingDistance = distanceTotal;
        double remainingAlpha = alphaTotal;
        double remainingTime;

        Point2D.Double currentPosition;
        double currentAlpha;
        double ratioMove;
        TrajectoryCurve deltaCurve = new TrajectoryCurve(curve.getLeftWheel(), curve.getRightWheel());
        deltaCurve.setResults(curve.getAngle(), radius, curve.getDistance(), direction);
        double deltaDistance;
        double deltaAlpha;

        // TODO passer � une simple boucle while
        // boucle qui met à jour les données toutes les "sleepTime" ms evniron
        do {
            try {
                remainingTime = getEstimatedTime(remainingCurve);
                if (remainingTime < sleepTime) {
                    sleepTime = remainingTime;
                }
                Thread.sleep((long) sleepTime);
                // sauvegarde de la dernière position et de la dernière
                // orientation
                currentPosition = (Point2D.Double) movedElement.getPosition().clone();
                currentAlpha = movedElement.getOrientation();
                RobotPosition newPosition = new RobotPosition(currentPosition.getX(), currentPosition.getY(),
                        currentAlpha);

                // TODO calculer le ratioMove en fonction du temps r��l
                // d'attente du Thread qui peut �tre plus long que sleep
                // decomposition de la courbe
                if (distanceTotal != 0) {
                    ratioMove = (Math.min(distanceRefresh, Math.abs(remainingDistance))) / distanceTotal;
                    ratioMove = Math.abs(ratioMove);
                } else {
                    ratioMove = (Math.min(alphaRefresh, Math.abs(remainingAlpha))) / alphaTotal;
                    ratioMove = Math.abs(ratioMove);
                }
                deltaAlpha = alphaTotal * ratioMove;
                deltaDistance = distanceTotal * ratioMove;
                deltaCurve.setResults(deltaAlpha, radius, deltaDistance, direction);

                // calcul de la nouvelle position
                updatePosition(deltaCurve, newPosition);

                if (testCollision(newPosition, movedElement)) {
                    firePosition(PositionStatus.FAILED);
                    return;
                }

                // update the robot position on the gameboard
                double newX = newPosition.getCentralPoint().getX();
                double newY = newPosition.getCentralPoint().getY();
                double newAlpha = newPosition.getAlpha();
                movedElement.setPosition(new Point2D.Double(newX, newY));
                movedElement.setOrientation(newAlpha);

                // remainingDistance -= deltaDistance;
                remainingCurve.setResults(remainingCurve.getAngle() - deltaAlpha, radius, remainingCurve.getDistance()
                        - deltaDistance, direction);
                remainingDistance = remainingCurve.getDistance();
                remainingAlpha = remainingCurve.getAngle();
                if ((remainingDistance < 0 && distanceTotal > 0) || (remainingDistance > 0 && distanceTotal < 0)) {
                    remainingDistance = 0;
                }
                if ((remainingAlpha < 0 && alphaTotal > 0) || (remainingAlpha > 0 && alphaTotal < 0)) {
                    remainingAlpha = 0;
                }
                remainingCurve.setResults(remainingAlpha, radius, remainingDistance, direction);

                firePosition(PositionStatus.MOVING);

            } catch (InterruptedException e) {
                System.out.println("erreur de sleep");
                e.printStackTrace();
            }
        } while ((remainingCurve.getDistance() != 0 || remainingCurve.getAngle() != 0) && !terminated);
        if (!terminated) {
            firePosition(PositionStatus.OK);
        } else {
            firePosition(PositionStatus.FAILED);
        }
    }

    public void setReadSpeed(int pReadSpeed) {
        time /= pReadSpeed;
        speed *= pReadSpeed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    synchronized public void setTerminated(boolean pTerminated) {
        this.terminated = pTerminated;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void shutdown() {
        setTerminated(true);
    }

    private Boolean testCollision(RobotPosition pPosition, IMovableElement pMovedElement) {
        // test collision bord
        double aGameBoardHeight = gamePlayBound.getHeight();
        double aGameBoardWidth = gamePlayBound.getWidth();
        double positionX = pPosition.getCentralPoint().getX();
        double positionY = pPosition.getCentralPoint().getY();

        if (positionY > aGameBoardHeight) {
            System.out.println("Collision on up");
            return true;
        } else if (positionY < 0) {
            System.out.println("Collision on bottom");
            return true;
        } else if (positionX > aGameBoardWidth) {
            System.out.println("Collision on right");
            return true;
        } else if (positionX < 0) {
            System.out.println("Collision on left");
            return true;
        }

        // TODO am�liorer les test de collision
        // test collision element
        Shape movedElementShape = pMovedElement.getAbsoluteBounds();
        Iterator<IGameBoardElement> iterator = elements.iterator();
        while (iterator.hasNext()) {
            IGameBoardElement element = iterator.next();

            if (element == movedElement || !element.isObstacle())
                continue;

            Shape obstacleBound = element.getBounds();
            Point2D obstaclePosition = element.getPosition();
            double obstacleOrientation = element.getOrientation();
            AffineTransform transform = new AffineTransform();
            transform.translate(obstaclePosition.getX(), obstaclePosition.getY());
            transform.rotate(obstacleOrientation);
            Shape obstacleShape = transform.createTransformedShape(obstacleBound);
            if (movedElementShape.intersects(obstacleShape.getBounds())) {
                System.out.println("Collision with an other element");
                return true;
            }
        }
        // si aucune collision
        return false;
    }

    // TODO methode de robotPosition, trouver un moyen de ne pas l'exterioriser
    private void transform(Point2D point, double angle, Point2D translation) {
        // Computes a rotation followed by a translation
        // This function is equivalent to (Rotation matrix) x (Translation
        // matrix) x (Vector)
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        double x = point.getX();
        double y = point.getY();
        point.setLocation(x * c - y * s + translation.getX(), x * s + y * c + translation.getY());
    }

    // TODO methode de robotPosition, trouver un moyen de ne pas l'exterioriser
    void updatePosition(TrajectoryCurve curve, RobotPosition pRoboPosition) {
        double d = curve.getDistance();
        double a = curve.getAngle();
        double r = curve.getRadius();

        // straight distance
        double sd;
        if (a != 0) {
            sd = Math.abs(2.0f * r * Math.sin(a / 2.0f));
        } else {
            sd = d;
        }

        // move from current location in robot coordinates
        double t = a / 2.0f;
        double dx = sd * Math.cos(t);
        double dy = sd * Math.sin(t);

        // TODO ne tient pas compte des angles supérieurs à PI / 2
        switch (curve.getDirection()) {
        case LEFT:
            if (a < 0) {
                dx = -dx;
                dy = -dy;
            }
            break;
        case RIGHT:
            if (a > 0) {
                dy = -dy;
            } else {
                dx = -dx;
            }
            a = -a;
            break;
        }

        // final orientation
        double finalOrientation = pRoboPosition.getAlpha() + a % (2 * Math.PI);

        Point2D end = new Point2D.Double(dx, dy);
        transform(end, finalOrientation, pRoboPosition.getCentralPoint());

        pRoboPosition.getCentralPoint().setLocation(end);
        pRoboPosition.setAlpha(finalOrientation);
    }
}
