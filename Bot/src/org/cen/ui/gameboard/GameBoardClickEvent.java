package org.cen.ui.gameboard;

import java.awt.geom.Point2D;

/**
 * Event sent when a click on the game board has occurred.
 * 
 * @author Emmanuel ZURMELY
 */
public class GameBoardClickEvent implements IGameBoardEvent {
	private Point2D coordinates;

	/**
	 * Constructor.
	 * 
	 * @param coordinates
	 *            coordinates of the click in real coordinates
	 */
	public GameBoardClickEvent(Point2D coordinates) {
		super();
		this.coordinates = coordinates;
	}

	/**
	 * The coordinates of the click in real coordinates.
	 * 
	 * @return the coordinates of the click
	 */
	public Point2D getCoordinates() {
		return coordinates;
	}

	@Override
	public String toString() {
		return getClass().getName() + "[" + coordinates.toString() + "]";
	}
}
