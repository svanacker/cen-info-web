package org.cen.robot.match;

import java.awt.geom.Point2D;

/**
 * Elementary movement on the game board.
 * 
 * @author Emmanuel ZURMELY
 */
public class OpponentPosition {

	private double accuracy;

	private double direction;

	private Point2D location;

	private long timestamp;

	/**
	 * Constructor.
	 * 
	 * @param location
	 *            the location on the game board
	 * @param timestamp
	 *            the time stamp
	 * @param direction
	 *            the direction
	 * @param accuracy
	 *            the accuracy
	 */
	public OpponentPosition(Point2D location, long timestamp, double direction, double accuracy) {
		super();
		this.location = location;
		this.timestamp = timestamp;
		this.direction = direction;
		this.accuracy = accuracy;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof OpponentPosition) {
			OpponentPosition m = (OpponentPosition) obj;
			return location.equals(m.location) && timestamp == m.timestamp && direction == m.direction && accuracy == m.accuracy;
		}
		return super.equals(obj);
	}

	/**
	 * Returns the accuracy in the range [0 ; 1].
	 * 
	 * @return the accuracy in the range [0 ; 1]
	 */
	public double getAccuracy() {
		return accuracy;
	}

	/**
	 * Returns the direction angle in radians.
	 * 
	 * @return the direction angle in radians
	 */
	public double getDirection() {
		return direction;
	}

	/**
	 * Returns the location.
	 * 
	 * @return the location
	 */
	public Point2D getLocation() {
		return location;
	}

	/**
	 * Returns the time stamp.
	 * 
	 * @return the time stamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets the accuracy
	 * 
	 * @param accuracy
	 *            the accuracy in the range [0 ; 1]
	 */
	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}

	/**
	 * Sets the direction angle in radians.
	 * 
	 * @param direction
	 *            the direction angle in radians
	 */
	public void setDirection(double direction) {
		this.direction = direction;
	}

	/**
	 * Sets the location.
	 * 
	 * @param location
	 *            the location
	 */
	public void setLocation(Point2D location) {
		this.location = location;
	}

	/**
	 * Sets the time stamp.
	 * 
	 * @param location
	 *            the time stamp
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Returns the string representation of the receiver.
	 * 
	 * @return the string representation of the receiver
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[accuracy=" + accuracy + ", direction=" + direction + ", location=" + location
				+ ", timestamp=" + timestamp + "]";
	}
}
