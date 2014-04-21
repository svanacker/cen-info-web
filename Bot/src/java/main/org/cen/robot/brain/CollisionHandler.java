package org.cen.robot.brain;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.logging.Logger;

import org.cen.logging.LoggingUtils;
import org.cen.navigation.AbstractDeadZoneHandler;
import org.cen.robot.CollisionConfiguration;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.RobotDimension;
import org.cen.robot.RobotPosition;
import org.cen.robot.RobotUtils;
import org.cen.robot.device.RobotDeviceResult;
import org.cen.robot.device.collision.CollisionDetectionDevice;
import org.cen.robot.device.collision.CollisionReadResult;
import org.cen.robot.device.collision.OpponentMovedResult;
import org.cen.robot.match.IMatchStrategy;
import org.cen.robot.match.Opponent;
import org.cen.robot.match.events.CollisionDetectionEvent;
import org.cen.robot.match.events.OpponentMovedEvent;

public class CollisionHandler extends AbstractDeviceHandler {

	private static final Logger LOGGER = LoggingUtils.getClassLogger();

	private final AbstractDeadZoneHandler deadZoneHandler;

	private Area collisionArea;

	/**
	 * Constructor.
	 * 
	 * @param servicesProvider
	 */
	public CollisionHandler(IRobotServiceProvider servicesProvider,
			AbstractDeadZoneHandler deadZoneHandler) {
		super(servicesProvider);
		this.deadZoneHandler = deadZoneHandler;
		initCollisionArea();
		LOGGER.fine("Collision handler initialized");
	}

	private void checkCollision(IMatchStrategy strategy, OpponentMovedResult omr) {
		RobotPosition robotPosition = RobotUtils.getRobotAttribute(
				RobotPosition.class, servicesProvider);
		Point2D opponentPosition = omr.getData().getPosition();
		Point2D p = robotPosition.getCentralPoint();
		// Positionnement de la zone de collision par rapport à la position
		// courante du robot
		AffineTransform t = new AffineTransform();
		t.translate(p.getX(), p.getY());
		t.rotate(robotPosition.getAlpha());
		Shape area = t.createTransformedShape(collisionArea);
		// Vérification que l'adversaire n'est pas dans la zone
		if (area.contains(opponentPosition)) {
			// Notification de la collision
			strategy.notifyEvent(new CollisionDetectionEvent(opponentPosition));
		}
	}

	public AbstractDeadZoneHandler getDeadZoneHandler() {
		return deadZoneHandler;
	}

	@Override
	public String getDeviceName() {
		return CollisionDetectionDevice.NAME;
	}

	@Override
	public void handleResult(RobotDeviceResult result) {
		IMatchStrategy strategy = servicesProvider
				.getService(IMatchStrategy.class);
		if (result instanceof OpponentMovedResult) {
			OpponentMovedResult omr = (OpponentMovedResult) result;
			updateOpponent(omr);
			checkCollision(strategy, omr);
			strategy.notifyEvent(new OpponentMovedEvent(omr.getData()
					.getPosition()));
		} else if (result instanceof CollisionReadResult) {
			CollisionReadResult crr = (CollisionReadResult) result;
			strategy.notifyEvent(new CollisionDetectionEvent(crr
					.getObstaclePosition()));
		}
	}

	private void initCollisionArea() {
		RobotDimension dimensions = RobotUtils.getRobotAttribute(
				RobotDimension.class, servicesProvider);
		CollisionConfiguration configuration = RobotUtils.getRobotAttribute(
				CollisionConfiguration.class, servicesProvider);
		double d = configuration.getDistance();
		double w = dimensions.getWidth() + d * 2;
		double h = dimensions.getDepth() + d * 2;
		double o = configuration.getOffset();
		Arc2D.Double arc = new Arc2D.Double(-w / 2, -h / 2 + o, w, h,
				configuration.getStartAngle(), configuration.getExtent(),
				Arc2D.PIE);
		collisionArea = new Area(arc);
	}

	private void updateOpponent(OpponentMovedResult omr) {
		Opponent opponent = RobotUtils.getRobotAttribute(Opponent.class,
				servicesProvider);
		Point2D opponentPosition = omr.getData().getPosition();
		opponent.setLastLocation(opponentPosition);

		if (deadZoneHandler != null) {
			deadZoneHandler.handleDeadZone(opponentPosition);
		}
	}
}
