package org.cen.ui.gameboard;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;

/**
 * Abstract base class of a game board element.
 * 
 * @author Emmanuel ZURMELY
 */
public abstract class AbstractGameBoardElement implements IGameBoardElement {
	/**
	 * The bounds of the element.
	 */
	protected Shape bounds;

	/**
	 * The name of the element.
	 */
	protected String name;

	/**
	 * The drawing order of the element. The lower value are drawn first
	 */
	protected int order;

	/**
	 * The orientation of the element in radias.
	 */
	protected double orientation;

	/**
	 * The position of the element on the game board.
	 */
	protected Point2D position;

	/**
	 * Constructor
	 * 
	 * @param name
	 *            the name of the element
	 * @param position
	 *            the position of the element on the game board
	 */
	public AbstractGameBoardElement(String name, Point2D position) {
		this(name, position, 0);
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            the name of the element
	 * @param position
	 *            the position of the element on the game board
	 * @param orientation
	 *            the orientation of the element in radians
	 */
	public AbstractGameBoardElement(String name, Point2D position,
			double orientation) {
		super();
		this.name = name;
		this.position = position;
		this.orientation = orientation;
	}

	@Override
	public Shape getBounds() {
		return bounds;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getOrder() {
		return order;
	}

	@Override
	public double getOrientation() {
		return orientation;
	}

	@Override
	public Point2D getPosition() {
		return position;
	}

	@Override
	public boolean isMovable() {
		return false;
	}

	@Override
	public boolean isObstacle() {
		return false;
	}

	@Override
	public void paintUnscaled(Graphics2D g) {
		// default implementation does nothing
	}
}
