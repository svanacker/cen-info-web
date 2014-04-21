package org.cen.robot.device.navigation;

import java.awt.geom.Point2D;

/**
 * Encapsulate a bezier move request.
 */
public class BezierMoveRequest extends NavigationRequest {

    private final Point2D destination;

    private final double d1;

    private final double d2;

    private final double angle;

    public BezierMoveRequest(Point2D destination, double d1, double d2, double angle) {
        super();
        this.destination = destination;
        this.d1 = d1;
        this.d2 = d2;
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }

    public double getD1() {
        return d1;
    }

    public double getD2() {
        return d2;
    }

    public Point2D getDestination() {
        return destination;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[destination=" + destination + ", d1=" + d1 + ", d2=" + d2 + ", angle="
                + angle + "]";
    }
}
