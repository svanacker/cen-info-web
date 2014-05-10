package org.cen.robot.match.events;

import org.cen.geom.Point2D;

import org.cen.robot.match.IMatchEvent;

/**
 * Event signaling a potential collision.
 * 
 * @author Emmanuel ZURMELY
 */
public class CollisionDetectionEvent implements IMatchEvent {
	private Point2D position;

	/**
	 * Constructor.
	 * 
	 * @param position
	 *            the position of the obstacle
	 */
	public CollisionDetectionEvent(Point2D position) {
		super();
		this.position = position;
	}

	/**
	 * Returns the position of the detected obstacle.
	 * 
	 * @return the position of the detected obstacle
	 */
	public Point2D getPosition() {
		return position;
	}
}
