package org.cen.math;

import java.awt.geom.Dimension2D;

/**
 * A Dimension2D implementation with double precision resolution.
 * 
 * @author Emmanuel ZURMELY
 * @version 13/02/2007
 */
public class Size2D extends Dimension2D {
	private double h;

	private double w;

	/**
	 * Constructor.
	 * 
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
	public Size2D(double width, double height) {
		setSize(width, height);
	}

	@Override
	public double getHeight() {
		return h;
	}

	@Override
	public double getWidth() {
		return w;
	}

	@Override
	public void setSize(double width, double height) {
		w = width;
		h = height;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + w + ", " + h + "]";
	}
}
