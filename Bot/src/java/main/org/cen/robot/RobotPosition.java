package org.cen.robot;

import java.awt.geom.Point2D;
import java.util.Properties;

import org.cen.navigation.TrajectoryCurve;
import org.cen.navigation.TrajectoryCurve.Direction;

/*
 * Game Area : 
 * 
 * Y__________
 * |         |
 * |         |
 * |    ^    |
 * |    |    |
 * |    |    |
 * |         |
 * |_________|
 *            X
 * The alpha is defined as the angle between Y and the array as in the schema            
 * The X and Y are defined for the center of the robot            
 */

/**
 * Describes the position of the robot in X, Y, and Alpha The area is described
 * as following : X and Y are in mm Alpha corresponds to the trigonometric angle
 * with the horizontal x axis.
 * 
 * @author svanacker
 * @version 23/02/2007
 */
public class RobotPosition implements IRobotAttribute {
	private static final String PROPERTY_X = "x";

	private static final String PROPERTY_Y = "y";

	private static final String PROPERTY_ALPHA = "alphaDegrees";

	/** The position of the robot for Alpha */
	protected double alpha;

	/** The position of the robot for X axis */
	protected Point2D.Double centralPoint = new Point2D.Double();

	/**
	 * Constructor with all Values
	 * 
	 * @param x
	 * @param y
	 * @param alpha
	 */
	public RobotPosition(double x, double y, double alpha) {
		super();
		set(x, y, alpha);
	}

	void computeCurve(TrajectoryCurve curve, double width) {
		if (Math.signum(curve.getLeftWheel()) == Math.signum(curve.getRightWheel())) {
			// ICR is outside of the wheels
			computeLargeRadiusCurve(curve, width);
		} else {
			// ICR is between the wheels
			computeNarrowRadiusCurve(curve, width);
		}
	}

	void computeLargeRadiusCurve(TrajectoryCurve curve, double width) {
		// min and max curve distance values
		double min, max;
		Direction direction;
		double left = curve.getLeftWheel();
		double right = curve.getRightWheel();
		int c = Double.compare(Math.abs(left), Math.abs(right));
		switch (c) {
		case 0:
			// straight (abs(left) = abs(right))
			curve.setResults(0, Double.MAX_VALUE, left, Direction.LEFT);
			return;
		case 1:
			// turn right (abs(left) > abs(right))
			min = right;
			max = left;
			direction = Direction.RIGHT;
			break;
		default:
			// turn left (abs(left) < abs(right))
			min = left;
			max = right;
			direction = Direction.LEFT;
			break;
		}
		double theta = max - min;
		double angle = theta / width;
		double radius = Math.abs((width / 2.0f) * (max + min) / theta);
		double distance = angle * radius;
		curve.setResults(angle, radius, distance, direction);
	}

	void computeNarrowRadiusCurve(TrajectoryCurve curve, double width) {
		// min and max curve distance values
		double max, min;
		double left = curve.getLeftWheel();
		double right = curve.getRightWheel();
		Direction direction;
		int c = Double.compare(Math.abs(left), Math.abs(right));
		switch (c) {
		case 0:
			// straight (abs(left) = abs(right))
			if (Double.compare(left, right) == 0) {
				curve.setResults(0, Double.MAX_VALUE, left, Direction.LEFT);
				return;
			} else {
				min = Math.min(left, right);
				max = Math.max(left, right);
				if (min == left) {
					direction = Direction.LEFT;
				} else {
					direction = Direction.RIGHT;
				}
			}
		case 1:
			// turn right (abs(left) > abs(right))
			max = left;
			min = right;
			direction = Direction.RIGHT;
			break;
		default:
			// turn left (abs(left) < abs(right))
			min = left;
			max = right;
			direction = Direction.LEFT;
			break;
		}
		double theta = Math.abs(min) + Math.abs(max);
		double angle = (theta / width) * Math.signum(max);
		double radius = (width / 2.0f) * (Math.abs(max) - Math.abs(min)) / theta;
		double distance = angle * radius;
		curve.setResults(angle, radius, distance, direction);
	}

	public Point2D getAbsolutePostion(Point2D relativePosition) {
		Point2D.Double p = new Point2D.Double();
		double dx = relativePosition.getX();
		double dy = relativePosition.getY();
		double distance = Math.sqrt(dx * dx + dy * dy);
		double angle = alpha - Math.atan2(dx, dy);
		p.x = centralPoint.x + distance * Math.cos(angle);
		p.y = centralPoint.y + distance * Math.sin(angle);
		return p;
	}

	public double getAlpha() {
		return alpha;
	}

	public Point2D getCentralPoint() {
		return centralPoint;
	}

	/**
	 * Returns the position of the point at the given distance of the central
	 * point of the robot.
	 * 
	 * @param distance
	 *            the distance in millimeters
	 * @return the position of the point at the given distance of the central
	 *         point of the robot.
	 */
	public Point2D getPointAt(double distance) {
		Point2D.Double p = new Point2D.Double();
		p.x = centralPoint.x + distance * Math.cos(alpha);
		p.y = centralPoint.y + distance * Math.sin(alpha);
		return p;
	}

	/**
	 * Computes the new position of the robot with a distance in mm
	 * 
	 * @param distance
	 *            the distance in millimeters
	 */
	public void go(double distance) {
		centralPoint.x += distance * Math.cos(alpha);
		centralPoint.y += distance * Math.sin(alpha);
	}

	/**
	 * Computes the new position of the robot with a distance exprimed in number
	 * of pulse
	 */
	public void goPulse(double pulse, RobotDimension dimension) {
		double distance = (dimension.getLeftMotor().pulseToDistance(pulse) + dimension.getRightMotor().pulseToDistance(pulse)) / 2;
		go(distance);
	}

	/**
	 * Computes the new position after a rotation with a angle in radians
	 * 
	 * @param angle
	 *            the angle of rotation
	 */
	public void rotateAngle(double angle) {
		alpha += angle;
		alpha %= (Math.PI * 2);
	}

	/**
	 * Computes the new position after a rotation with a wheelDistance in mm
	 * 
	 * @param distance
	 *            the distance of a wheel in mm
	 * @param dimension
	 *            the dimension of the robot
	 */
	public void rotateDistance(double distance, RobotDimension dimension) {
		double angle = distance / dimension.getWheelDistance() * 2;
		rotateAngle(angle);
	}

	/**
	 * Computes the new position after a rotation in pulse
	 * 
	 * @param pulseCount
	 *            the number of pulse that the robot do
	 * @param dimension
	 *            the dimension of the robot
	 */
	public void rotateDistancePulse(double pulseCount, RobotDimension dimension) {
		double distance = (dimension.getLeftMotor().pulseToDistance(pulseCount) + dimension.getRightMotor().pulseToDistance(pulseCount)) / 2;
		rotateDistance(distance, dimension);
	}

	public void set(double x, double y, double alpha) {
		this.centralPoint.x = x;
		this.centralPoint.y = y;
		this.alpha = alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public void setCentralPoint(Point2D.Double centralPoint) {
		this.centralPoint = centralPoint;
	}

	public void setFromProperties(Properties properties, String prefix) {
		String value = properties.getProperty(prefix + PROPERTY_X);
		double x = Double.valueOf(value);
		value = properties.getProperty(prefix + PROPERTY_Y);
		double y = Double.valueOf(value);
		value = properties.getProperty(prefix + PROPERTY_ALPHA);
		double alpha = Math.toRadians(Double.valueOf(value));
		set(x, y, alpha);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[x=" + centralPoint.x + ", y=" + centralPoint.y + ", alpha=" + Math.toDegrees(alpha) + "]";
	}

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

	public void updateFromPulses(long left, long right, RobotDimension dimension) {
		double l = dimension.getLeftMotor().pulseToDistance(left);
		double r = dimension.getRightMotor().pulseToDistance(right);
		TrajectoryCurve curve = new TrajectoryCurve(l, r);
		computeCurve(curve, dimension.getWheelDistance());
		updatePosition(curve);
	}

	void updatePosition(TrajectoryCurve curve) {
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
		double finalOrientation = alpha + a % (2 * Math.PI);

		Point2D end = new Point2D.Double(dx, dy);
		transform(end, finalOrientation, centralPoint);

		centralPoint.setLocation(end);
		alpha = finalOrientation;
	}
}
