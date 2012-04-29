package org.cen.navigation;

import java.awt.Point;

/**
 * Object representing a location on the game board.
 * 
 * @author Emmanuel ZURMELY
 */
public class Location {
	/**
	 * Name of the location.
	 */
	protected final String name;

	/**
	 * x coordinate.
	 */
	protected final int x;

	/**
	 * y coordinate.
	 */
	protected final int y;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            the name of the location
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 */
	public Location(String name, int x, int y) {
		super();
		this.name = name;
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	/**
	 * Return the distance to the specified location
	 * 
	 * @param l
	 *            a location
	 * @return the distance from this location to the specified location
	 */
	public int getDistance(Location l) {
		int dx = x - l.x;
		int dy = y - l.y;
		return (int) Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * Returns the name of the location.
	 * 
	 * @return the name of the location
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the position of this location
	 * 
	 * @return the position of this location
	 */
	public Point getPosition() {
		return new Point(x, y);
	}

	/**
	 * Returns the x coordinate of this location.
	 * 
	 * @return the x coordinate of this location
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns the y coordinate of this location.
	 * 
	 * @return the y coordinate of this location
	 */
	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[name: " + name + ", x: " + x + ", y: " + y + "]";
	}
}
