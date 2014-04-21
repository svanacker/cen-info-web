package org.cen.ui.gameboard.elements.util;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 * Useful method to draw
 * 
 */
public class DrawHelper {

	// Rectangle

	/**
	 * Draw a rectangle filled and centered at (x, y) with width and height
	 * 
	 * @param g
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public static void fillCenteredRectangle(Graphics2D g, double x, double y, double width, double height) {
		Shape rectangle = new Rectangle2D.Double(x - width / 2, y - height / 2, width, height);
		g.fill(rectangle);
	}

	/**
	 * Draw a rectangle filled and centered at (0, 0) with width and height
	 * 
	 * @param g
	 * @param width
	 * @param height
	 */
	public static void fillCenteredRectangle(Graphics2D g, double width, double height) {
		fillCenteredRectangle(g, 0d, 0d, width, height);
	}

	/**
	 * Draw a rectangle filled and centered at (x, y) with width and height
	 * 
	 * @param g
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public static void drawCenteredRectangle(Graphics2D g, double x, double y, double width, double height) {
		Shape rectangle = new Rectangle2D.Double(x - width / 2, y - height / 2, width, height);
		g.draw(rectangle);
	}

	// Circle

	/**
	 * Draw a circle filled and centered at (x, y) with diameter
	 * 
	 * @param g
	 * @param x
	 * @param y
	 * @param diameter
	 */
	public static void fillCenteredCircle(Graphics2D g, double x, double y, double diameter) {
		Shape circle = new Ellipse2D.Double(x - diameter / 2d, y - diameter / 2d, diameter, diameter);
		g.fill(circle);
	}

	/**
	 * Draw a circle filled and centered at (0d, 0d) with diameter
	 * 
	 * @param g
	 * @param diameter
	 */
	public static void fillCenteredCircle(Graphics2D g, double diameter) {
		fillCenteredCircle(g, 0d, 0d, diameter);
	}

	/**
	 * Draw a circle and centered at (x, y) with diameter
	 * 
	 * @param g
	 * @param x
	 * @param y
	 * @param diameter
	 */
	public static void drawCenteredCircle(Graphics2D g, double x, double y, double diameter) {
		Shape circle = new Ellipse2D.Double(x - diameter / 2d, y - diameter / 2d, diameter, diameter);
		g.draw(circle);
	}

	/**
	 * Draw a circle centered at (0d, 0d) with diameter
	 * 
	 * @param g
	 * @param diameter
	 */
	public static void drawCenteredCircle(Graphics2D g, double diameter) {
		drawCenteredCircle(g, 0d, 0d, diameter);
	}
}
