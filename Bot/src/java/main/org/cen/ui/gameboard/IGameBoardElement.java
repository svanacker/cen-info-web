package org.cen.ui.gameboard;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;

/**
 * Interface representing an element of the game board.
 * 
 * @author Emmanuel ZURMELY
 */
public interface IGameBoardElement {
	/**
	 * Returns the bounds of this element.
	 * 
	 * @return the bounds of this element
	 */
	public Shape getBounds();

	/**
	 * Returns the name of the element.
	 * 
	 * @return the name of the element
	 */
	public String getName();

	/**
	 * Returns the painting order of the element.
	 * 
	 * @return the painting order of the element
	 */
	public int getOrder();

	/**
	 * Returns the orientation angle of the element in radians.
	 * 
	 * @return the orientation angle of the element
	 */
	public double getOrientation();

	/**
	 * Returns the position of the element on the gameboard.
	 * 
	 * @return the position of the element on the gameboard
	 */
	public Point2D getPosition();

	/**
	 * Determines whether this element is movable on the gameboard
	 * 
	 * @return true if this element can be moved during the game, false
	 *         otherwise
	 */
	public boolean isMovable();

	/**
	 * Determines whether this element is an obstacle for the robots
	 * 
	 * @return true if this element represents an obstacle for the robots, false
	 *         otherwise
	 */
	public boolean isObstacle();

	/**
	 * Paints the element on the specified graphic device.
	 * 
	 * @param g
	 *            the graphic device to paint on
	 */
	public void paint(Graphics2D g);

	/**
	 * Paints the element using the screen coordinates.
	 * 
	 * @param g
	 *            the graphic device to paint on
	 */
	public void paintUnscaled(Graphics2D g);
}
