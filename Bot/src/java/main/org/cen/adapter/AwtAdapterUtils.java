package org.cen.adapter;

import org.cen.geom.Point2D;

public class AwtAdapterUtils {

    public static java.awt.geom.Point2D toAwtPoint2D(Point2D point2d) {
        return new java.awt.geom.Point2D.Double(point2d.getX(), point2d.getY());
    }
}
