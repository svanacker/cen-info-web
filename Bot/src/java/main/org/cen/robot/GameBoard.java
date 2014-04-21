package org.cen.robot;

import org.cen.navigation.AbstractNavigationMap;
import org.cen.navigation.INavigationMap;
import org.cen.navigation.Location;

/**
 * Describe the board where the robot goes and play xLength and yLength are
 * exprimed in mm.
 * 
 * @author Stephane
 * @version 23/02/2007
 */
@Deprecated
public abstract class GameBoard {

	/** Width of the Board */
	protected double xLength;

	/** Height of the Board */
	protected double yLength;

	/** Size of the grid */
	protected double gridSize;

	/**
	 * Return the length following Y Axis
	 * 
	 * @return the length following Y Axis
	 */
	public double getYLength() {
		return yLength;
	}

	/** Carte de navigation */
	protected INavigationMap navigationMap;

	/**
	 * Default Constructor
	 * 
	 */
	public GameBoard() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param xLength
	 *            the length of the board following X Axis in mm
	 * @param yLength
	 *            the length of the board following Y Axis in mm
	 * @param gridSize
	 *            the size of grid for navigation defined in mm
	 */
	public GameBoard(double xLength, double yLength, double gridSize) {
		super();
		this.xLength = xLength;
		this.yLength = yLength;
		this.gridSize = gridSize;
		navigationMap = new AbstractNavigationMap() {
		};
		initMap();
	}

	/**
	 * Init the map defined with the gridSize
	 */
	protected void initMap() {
		int cx = (int) (xLength / gridSize) + 1;
		int cy = (int) (yLength / gridSize) + 1;
		Location[] l = new Location[cx * cy];
		for (int i = 0; i < cx; i++)
			for (int j = 0; j < cy; j++) {
				l[i * cy + j] = new Location("x" + i + " y" + j, (int) (i * gridSize), (int) (j * gridSize));
				navigationMap.addLocation(l[i * cy + j]);
			}

		for (int x = 0; x < cx; x++)
			for (int y = 0; y < cy; y++)
				for (int j = 0; j <= 1; j++)
					for (int k = -1; k <= 1; k++) {
						if (j == 0 && k <= 0)
							continue;
						int xx = x + k;
						int yy = y + j;
						if (xx >= 0 && xx < cx && yy >= 0 && yy < cy && !(j == 0 && k == 0))
							navigationMap.addVector(l[x * cy + y], l[xx * cy + yy]);
					}
	}

	public double getGridSize() {
		return gridSize;
	}

	public void setGridSize(double gridSize) {
		this.gridSize = gridSize;
	}

	/**
	 * Return the length following X Axis
	 * 
	 * @return the length following X Axis
	 */
	public double getXLength() {
		return xLength;
	}

	public void setXLength(double length) {
		xLength = length;
	}

	public void setYLength(double length) {
		yLength = length;
	}

	public INavigationMap getNavigationMap() {
		return navigationMap;
	}

	public void setNavigationMap(INavigationMap navigationMap) {
		this.navigationMap = navigationMap;
	}
}
