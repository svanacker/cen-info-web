package org.cen.navigation;

import java.awt.geom.Point2D;

import javax.vecmath.Vector2d;

public class ControlPoint {

    private final Point2D point;

    private final String commands;

    private final boolean enabled = true;

    public ControlPoint(Point2D point, String commands) {
        super();
        this.point = point;
        this.commands = commands;
    }

    public String getCommands() {
        return commands;
    }

    public Point2D getPoint() {
        return point;
    }

    public Point2D getSegmentPoint(PathSegment segment) {
        Point2D p1 = segment.getStart().getPosition();
        Point2D p2 = segment.getEnd().getPosition();
        double x1 = p1.getX();
        double y1 = p1.getY();
        double sx = p2.getX() - x1;
        double sy = p2.getY() - y1;
        Vector2d v1 = new Vector2d(sx, sy);
        Vector2d v2 = new Vector2d(point.getX() - x1, point.getY() - y1);
        double cangle = Math.cos(v1.angle(v2));
        double l = (v2.length() / v1.length()) * cangle;
        return new Point2D.Double(x1 + l * sx, y1 + l * sy);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean mustStop() {
        return true;
    }
}
