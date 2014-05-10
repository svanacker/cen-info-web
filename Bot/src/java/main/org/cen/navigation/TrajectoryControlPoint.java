package org.cen.navigation;

import org.cen.geom.Point2D;

import org.cen.robot.TrajectoryCurve;

/**
 * Object representing a control point of a trajectory.
 * 
 * @author Emmanuel ZURMELY
 */
public class TrajectoryControlPoint {

    /**
     * Trajectory curve.
     */
    private final TrajectoryCurve curve;

    /**
     * Final orientation angle in radians.
     */
    private final double endOrientation;

    /**
     * Final position.
     */
    private final Point2D endPosition;

    /**
     * Instant center of rotation.
     */
    private final Point2D icr;

    /**
     * Initial orientation angle in radians.
     */
    private final double startOrientation;

    /**
     * Initial position.
     */
    private final Point2D startPosition;

    /**
     * Constructor.
     * 
     * @param curve
     *            the trajectory curve
     * @param startPosition
     *            the initial position
     * @param endPosition
     *            the final position
     * @param startOrientation
     *            the initial orientation angle in radians
     * @param endOrientation
     *            the final orientation angle in radians
     * @param icr
     *            the instant center of rotation
     */
    public TrajectoryControlPoint(TrajectoryCurve curve, Point2D startPosition, Point2D endPosition,
            double startOrientation, double endOrientation, Point2D icr) {
        super();
        this.curve = curve;
        this.endOrientation = endOrientation;
        this.endPosition = endPosition;
        this.icr = icr;
        this.startOrientation = startOrientation;
        this.startPosition = startPosition;
    }

    /**
     * Return the trajectory curve.
     * 
     * @return the trajectory curve
     */
    public TrajectoryCurve getCurve() {
        return curve;
    }

    /**
     * Return the final orientation.
     * 
     * @return the final orientation
     */
    public double getEndOrientation() {
        return endOrientation;
    }

    /**
     * Return the final position.
     * 
     * @return the final position
     */
    public Point2D getEndPosition() {
        return endPosition;
    }

    public Point2D getICR() {
        return icr;
    }

    /**
     * Return the initial orientation.
     * 
     * @return the initial orientation
     */
    public double getStartOrientation() {
        return startOrientation;
    }

    /**
     * Return the initial position.
     * 
     * @return the initial position
     */
    public Point2D getStartPosition() {
        return startPosition;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(getClass().getName());
        b.append("[icr: ");
        b.append(icr.toString());
        b.append(", startOrientation: ");
        b.append(startOrientation);
        b.append(", endOrientation: ");
        b.append(endOrientation);
        b.append(", start: ");
        b.append(startPosition);
        b.append(", end: ");
        b.append(endPosition);
        b.append("]");
        return b.toString();
    }
}
