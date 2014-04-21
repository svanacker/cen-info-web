package org.cen.ui.gameboard;

import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;

/**
 * Shape data wrapper.
 * 
 * @author Emmanuel ZURMELY.
 * 
 */
public class ShapeData {
	private final Paint paint;

	private final Shape shape;

	private final Stroke stroke;

	/**
	 * Constructor.
	 * 
	 * @param shape
	 *            the shape object
	 * @param stroke
	 *            the stroke object
	 * @param paint
	 *            the paint object
	 */
	public ShapeData(Shape shape, Stroke stroke, Paint paint) {
		super();
		this.shape = shape;
		this.stroke = stroke;
		this.paint = paint;
	}

	/**
	 * Returns the paint object to use to draw the shape.
	 * 
	 * @return the paint object to use to draw the shape
	 */
	public Paint getPaint() {
		return paint;
	}

	/**
	 * Returns the shape object.
	 * 
	 * @return the shape object
	 */
	public Shape getShape() {
		return shape;
	}

	/**
	 * Returns the stroke to use to draw the shape.
	 * 
	 * @return the stroke to use to draw the shape
	 */
	public Stroke getStroke() {
		return stroke;
	}
}