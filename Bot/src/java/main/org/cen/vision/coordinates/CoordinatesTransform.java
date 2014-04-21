package org.cen.vision.coordinates;

import java.awt.geom.Point2D;

import org.cen.robot.RobotPosition;

public interface CoordinatesTransform {
	public Point2D absoluteToScreen(RobotPosition position, Point2D p);

	/**
	 * Converts 2D coordinates from the game board (z=0) to 2D screen
	 * coordinates.
	 * 
	 * @param x
	 *            x coordinate in true length
	 * @param y
	 *            y coordinate in true length
	 * @return coordinates on the screen
	 */
	public abstract Point2D gameBoardToScreen(double x, double y);

	public Point2D screenToAbsolute(RobotPosition position, int x, int y);

	/**
	 * Converts 2D screen coordinates to 3D coordinates projected on the plan
	 * z=0.
	 * 
	 * @param x
	 *            x coordinate in pixels
	 * @param y
	 *            y coordinate in pixels
	 * @return coordinates on the plan z=0
	 */
	public abstract Point2D screenToGameBoard(int x, int y);
}