package org.cen.math;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.vecmath.Vector2d;

/**
 * Helper class for manipulating angles.
 */
public final class Angle {

	/**
	 * Returns the angular distance between two angles.
	 * 
	 * @param angle1
	 *            first angle in radians
	 * @param angle2
	 *            second angle in radians
	 * @return the angular distance between the two angles in radians in the
	 *         range [0 ; Pi]
	 */
	public static double getDistance(double angle1, double angle2) {
		double d = Math.abs(angle1 - angle2);
		if (d > Math.PI) {
			d = Math.PI * 2 - d;
		}
		return d;
	}

	/**
	 * Returns the mean value of an array of angles.
	 * 
	 * @param angles
	 *            an array of angles
	 * @return the mean value of the angles in radians in the range [0 ; 2 * Pi]
	 */
	public static double getMean(double[] angles) {
		double mx = 0, my = 0;
		// Moyenne des vecteurs
		for (double a : angles) {
			mx += Math.cos(a);
			my += Math.sin(a);
		}
		double d = Math.atan2(my, mx);
		if (d < 0) {
			d += Math.PI * 2;
		}
		return d;
	}

	/**
	 * Returns the angle between the segments [o, p1] and [o, p2]
	 * 
	 * @param o
	 *            the origin
	 * @param p1
	 *            point 1
	 * @param p2
	 *            point 2
	 * @return the angle p1,o,p2
	 */
	public static double getPointsAngle(Point2D o, Point2D p1, Point2D p2) {
		double x = o.getX();
		double y = o.getY();
		double x1 = p1.getX();
		double y1 = p1.getY();
		double x2 = p2.getX();
		double y2 = p2.getY();
		Vector2d v1 = new Vector2d(x1 - x, y1 - y);
		Vector2d v2 = new Vector2d(x2 - x, y2 - y);
		return v1.angle(v2) * Line2D.relativeCCW(x, y, x2, y2, x1, y1);
	}

	/**
	 * Returns the variation of an array of angles.
	 * 
	 * @param angles
	 *            an array of angles
	 * @return the variation of the angles in radians in the range [0 ; Pi]
	 */
	public static double getVariation(double[] angles) {
		double m = getMean(angles);
		double v = 0;
		for (double a : angles) {
			v += getDistance(m, a);
		}
		return v / angles.length;
	}
}
