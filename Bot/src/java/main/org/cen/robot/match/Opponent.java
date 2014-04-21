package org.cen.robot.match;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.cen.robot.IRobotAttribute;

/**
 * Object representing the opponent.
 * 
 * @author Emmanuel ZURMELY
 */
public class Opponent implements IRobotAttribute {

	private double lastDirection;

	private Point2D lastLocation;

	private long lastTimestamp;

	private List<OpponentPosition> movementsHistory = new ArrayList<OpponentPosition>();

	/**
	 * Adds a location.
	 * 
	 * @param coordinates
	 *            coordinates of the location of the opponent on the gameboard
	 */
	public void addLocation(Point2D coordinates) {
		addLocation(coordinates, System.currentTimeMillis());
	}

	private void addLocation(Point2D coordinates, long timestamp) {
		OpponentPosition movement = new OpponentPosition(coordinates, timestamp, 0, 0);
		computeMovement(movement);
		movementsHistory.add(movement);
		lastDirection = movement.getDirection();
		lastLocation = movement.getLocation();
		lastTimestamp = movement.getTimestamp();
	}

	private void computeMovement(OpponentPosition movement) {
		if (lastLocation != null) {
			Point2D newLocation = movement.getLocation();
			double dy = newLocation.getY() - lastLocation.getY();
			double dx = newLocation.getX() - lastLocation.getX();
			movement.setDirection(Math.atan2(dy, dx));
		}
	}

	/**
	 * Estimates the next movement and returns the estimated accuracy of the
	 * prediction in [0;1]
	 * 
	 * @param movements
	 *            a list of movements
	 */
	public void estimateNextMovement(List<OpponentPosition> movements) {
		// Gère les choix de stratégie en fonction du mouvement en cours de
		// l'adversaire
	}

	/**
	 * Returns the last known direction angle in radians.
	 * 
	 * @return the last known direction angle in radians
	 */
	public double getLastDirection() {
		return lastDirection;
	}

	/**
	 * Returns the last known location.
	 * 
	 * @return the last known location
	 */
	public Point2D getLastLocation() {
		return lastLocation;
	}

	/**
	 * Returns the time stamp of the last known location.
	 * 
	 * @return the time stamp of the last known location
	 */
	public long getLastTimeStamp() {
		return lastTimestamp;
	}

	/**
	 * Returns the movement history.
	 * 
	 * @return the movement history
	 */
	public List<OpponentPosition> getMovementsHistory() {
		return movementsHistory;
	}

	/**
	 * Determines whether the specified location is free in the given radius
	 * 
	 * @param location
	 *            the location center
	 * @param radius
	 *            the radius to check
	 * @return TRUE if the opponent overlaps the targeted location, FALSE
	 *         otherwise
	 */
	public boolean isLocationFree(Point2D location, double radius) {
		// Gère les collisions et les mouvements immédiats
		return lastLocation.distance(location) < radius;
	}

	private void rebuildHistory() {
		List<OpponentPosition> movements = new ArrayList<OpponentPosition>(movementsHistory);
		movementsHistory.clear();
		lastLocation = null;
		for (OpponentPosition m : movements) {
			addLocation(m.getLocation(), m.getTimestamp());
		}
	}

	/**
	 * Removes the specified movement from the history.
	 * 
	 * @param movement
	 *            the movement to remove from the history
	 */
	public void removeMovement(OpponentPosition movement) {
		movementsHistory.remove(movement);
		rebuildHistory();
	}

	/**
	 * Sets the last known direction angle in radians.
	 * 
	 * @param lastDirection
	 *            the direction angle in radians
	 */
	public void setLastDirection(double lastDirection) {
		this.lastDirection = lastDirection;
	}

	/**
	 * Sets the last known location of the opponent on the gameboard.
	 * 
	 * @param lastDirection
	 *            the location of the opponent on the gameboard
	 */
	public void setLastLocation(Point2D lastLocation) {
		this.lastLocation = lastLocation;
	}

	/**
	 * Sets the time stamp of the last known location of the opponent
	 * 
	 * @param lastTimestamp
	 *            the time stamp
	 */
	public void setLastTimeStamp(long lastTimestamp) {
		this.lastTimestamp = lastTimestamp;
	}

	/**
	 * Sets the movements history
	 * 
	 * @param movementsHistory
	 *            the movements history
	 */
	public void setMovementsHistory(List<OpponentPosition> movementsHistory) {
		this.movementsHistory = movementsHistory;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[lastDirection=" + lastDirection + ", lastLocation=" + lastLocation
				+ ", lastTimestamp=" + lastTimestamp + "]";
	}
}
