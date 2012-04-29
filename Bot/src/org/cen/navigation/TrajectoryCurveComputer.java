package org.cen.navigation;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.cen.navigation.TrajectoryCurve.Direction;

/**
 * Trajectory curves computer. This object computes the properties (initial and
 * final positions, initial and final orientations, angle and direction) of a
 * trajectory curve defined by recorded wheel distances.
 * 
 * @author Emmanuel ZURMELY
 */
public class TrajectoryCurveComputer {
	private List<TrajectoryCurve> curves = new ArrayList<TrajectoryCurve>();

	private double width;

	/**
	 * Constructor.
	 */
	public TrajectoryCurveComputer() {
		super();
	}

	/**
	 * Adds a curve to the trajectory.
	 * 
	 * @param curve
	 *            the curve to add
	 */
	public void addCurve(TrajectoryCurve curve) {
		curves.add(curve);
	}

	/**
	 * Computes the properties of a trajectory curve.
	 * 
	 * @param curve
	 *            the curve defined by wheel distances
	 */
	public void compute(TrajectoryCurve curve) {
		double left = curve.getLeftWheel();
		double right = curve.getRightWheel();
		double min, max;
		Direction direction;
		int c = Double.compare(Math.abs(left), Math.abs(right));
		switch (c) {
		case 1:
			// turn right
			min = right;
			max = left;
			direction = Direction.RIGHT;
			break;
		case 0:
			// straight
			curve.setResults(0d, Double.POSITIVE_INFINITY, left, Direction.LEFT);
			return;
		default:
			// turn left
			min = left;
			max = right;
			direction = Direction.LEFT;
			break;
		}
		double theta = Math.abs(max / min);
		double angle = min * (theta - 1) / width;
		double radius = width / (theta - 1) + (width / 2d);
		double distance = angle * radius;
		curve.setResults(angle, radius, distance, direction);
	}

	private TrajectoryControlPoint getControlPoint(TrajectoryCurve curve, Point2D start, double orientation) {
		double d = curve.getDistance();
		double a = curve.getAngle();
		double r = curve.getRadius();
		// straight distance
		double sd;
		if (a != 0) {
			sd = 2d * r * Math.sin(a / 2d);
		} else {
			sd = d;
		}
		// final orientation
		double endOrientation = orientation + a;
		// move from current location in robot coordinates
		double t = a / 2;
		double dx = sd * Math.cos(t);
		double dy = sd * Math.sin(t);
		if (curve.getDirection() == Direction.LEFT) {
			dx = -dx;
			r = -r;
		}
		// ICR has position (r, 0) in robot coordinates
		Point2D icr = new Point2D.Double(r, 0);
		// transform to absolute coordinates
		Point2D end = new Point2D.Double(dx, dy);
		AffineTransform tr = AffineTransform.getRotateInstance(orientation);
		tr.translate(start.getX(), start.getY());
		tr.transform(end, end);
		tr.transform(icr, icr);
		return new TrajectoryControlPoint(curve, start, end, orientation, endOrientation, icr);
	}

	/**
	 * Returns a list of trajectory control points computed from the trajectory
	 * curves defined by wheel distances relatively to a given initial position
	 * and orientation angle.
	 * 
	 * @param start
	 *            the initial point of the trajectory
	 * @param orientation
	 *            the initial orientation angle in radians
	 * @return the list trajectory control points
	 */
	public List<TrajectoryControlPoint> getTrajectory(Point2D start, double orientation) {
		List<TrajectoryControlPoint> result = new ArrayList<TrajectoryControlPoint>();
		for (TrajectoryCurve curve : curves) {
			compute(curve);
			TrajectoryControlPoint cp = getControlPoint(curve, start, orientation);
			result.add(cp);
			start = cp.getEndPosition();
			orientation = cp.getEndOrientation();
		}
		return result;
	}

	/**
	 * Returns the width of the wheels axis.
	 * 
	 * @return the width of the wheels axis
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Sets the width of the wheels axis.
	 * 
	 * @param width
	 *            the width of the wheels axis
	 */
	public void setWidth(double width) {
		this.width = width;
	}
}
