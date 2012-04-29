package org.cen.navigation;

/**
 * Object describing a trajectory curve.
 * 
 * @author Emmanuel ZURMELY
 */
public class TrajectoryCurve {
	/**
	 * The trajectory direction.
	 * 
	 * @author Emmanuel ZURMELY
	 */
	public enum Direction {
		/**
		 * Left side.
		 */
		LEFT,
		/**
		 * Right side.
		 */
		RIGHT;
	}

	private double angle;

	private Direction direction;

	private double distance;

	private double leftWheel;

	private double radius;

	private double rightWheel;

	/**
	 * Constructor.
	 * 
	 * @param leftWheel
	 *            left wheel distance
	 * @param rightWheel
	 *            right wheel distance
	 */
	public TrajectoryCurve(double leftWheel, double rightWheel) {
		super();
		setDistances(leftWheel, rightWheel);
	}

	private void clearResults() {
		angle = Double.NaN;
		distance = Double.NaN;
		direction = null;
	}

	/**
	 * Returns the angle of the trajectory in radians.
	 * 
	 * @return the angle of the trajectory in radians
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * Returns the direction of the trajectory.
	 * 
	 * @return the direction of the trajectory
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * Returns the distance.
	 * 
	 * @return the distance
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * Returns the left wheel distance.
	 * 
	 * @return the left wheel distance
	 */
	public double getLeftWheel() {
		return leftWheel;
	}

	/**
	 * Returns the radius of the curve.
	 * 
	 * @return the radius of the curve
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * Returns the right wheel distance.
	 * 
	 * @return the right wheel distance
	 */
	public double getRightWheel() {
		return rightWheel;
	}

	/**
	 * Sets the distances of the left and right wheels.
	 * 
	 * @param leftWheel
	 *            the left wheel distance
	 * @param rightWheel
	 *            the right wheel distance
	 */
	public void setDistances(double leftWheel, double rightWheel) {
		this.leftWheel = leftWheel;
		this.rightWheel = rightWheel;
		clearResults();
	}

	/**
	 * Sets the results.
	 * 
	 * @param angle
	 *            the trajectory angle in radians
	 * @param radius
	 *            the radius of the curve
	 * @param distance
	 *            the distance of the trajectory
	 * @param direction
	 *            the direction of the trajectory
	 */
	public void setResults(double angle, double radius, double distance, Direction direction) {
		this.angle = angle;
		this.radius = radius;
		this.distance = distance;
		this.direction = direction;
	}
}
