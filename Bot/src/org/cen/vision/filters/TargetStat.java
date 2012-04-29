package org.cen.vision.filters;

import java.awt.Point;

/**
 * Statistical data provided by the target filter.
 * 
 * @author Emmanuel ZURMELY
 */
public final class TargetStat {
	/**
	 * Data object describing the target location.
	 * 
	 * @author Emmanuel ZURMELY
	 */
	public static final class TargetLocation implements Comparable<TargetLocation> {
		private Point location;

		private double weight;

		/**
		 * Constructor.
		 * 
		 * @param location
		 *            location of the target
		 * @param value
		 *            weight of the target object
		 */
		public TargetLocation(Point location, double weight) {
			super();
			this.location = location;
			this.weight = weight;
		}

		/**
		 * Returns the location of the target in screen coordinates.
		 * 
		 * @return the loction of the target in screen coordinates
		 */
		public Point getLocation() {
			return location;
		}

		/**
		 * Return the weight associated to this target
		 * 
		 * @return the weight associated to this target
		 */
		public double getWeight() {
			return weight;
		}

		/**
		 * Sets the weight associated to this target
		 * 
		 * @param weight
		 *            the weight associated to this target
		 */
		public void setWeight(double weight) {
			this.weight = weight;
		}

		@Override
		public String toString() {
			return "x=" + location.x + ", y=" + location.y + ", weight=" + weight;
		}

		@Override
		public int compareTo(TargetLocation o) {
			return Double.compare(o.weight, weight);
		}
	}

	private double[][][] data;

	private String name;

	private int size;

	/**
	 * Returns the statistics data
	 * 
	 * @return the statistics data
	 */
	public double[][][] getData() {
		return data;
	}

	/**
	 * Returns the location of the best target in the given resolution grid.
	 * 
	 * @param index
	 *            index of the resolution grid
	 * @return the location of the best target
	 */
	public TargetLocation getMax(int index) {
		int n = data.length - 1;
		return getMax(index, 0, n, 0, n);
	}

	/**
	 * Returns the location of the best target in a sub-samble of the given
	 * resolution grid.
	 * 
	 * @param index
	 *            index of the resolution grid
	 * @param xmin
	 *            the low-bound x coordinate of the sub-sample
	 * @param xmax
	 *            the high-bound x coordinate of the sub-sample
	 * @param ymin
	 *            the low-bound y coordinate of the sub-sample
	 * @param ymax
	 *            the high-bound y coordinate of the sub-sample
	 * @return the location of the best target
	 */
	public TargetLocation getMax(int index, int xmin, int xmax, int ymin, int ymax) {
		Point p = new Point();
		double max = 0;
		for (int x = xmin; x <= xmax; x++)
			for (int y = ymin; y <= ymax; y++)
				if (data[index][x][y] > max) {
					max = data[index][x][y];
					p.x = x;
					p.y = y;
				}
		return new TargetLocation(p, max);
	}

	/**
	 * Retrieve the best target by seeking recursively into the resolution
	 * grids.
	 * 
	 * @return the location of the best target
	 */
	public TargetLocation getMaxRecursively() {
		int xmin = 0, ymin = 0, xmax = 7, ymax = 7;
		TargetLocation max = null;
		for (int i = 2; i < 5; i++) {
			max = getMax(i, xmin, xmax, ymin, ymax);
			Point p = max.getLocation();
			xmin = p.x * 2;
			ymin = p.y * 2;
			xmax = xmin + 1;
			ymax = ymin + 1;
		}
		return max;
	}

	/**
	 * Returns the name of this object.
	 * 
	 * @return the name of this object
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the size of the resolution grid.
	 * 
	 * @return the size of the resolution grid
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Sets the statistics data.
	 * 
	 * @param data
	 *            the statistics data
	 */
	public void setData(double[][][] data) {
		this.data = data;
	}

	/**
	 * Sets the name of this object.
	 * 
	 * @param name
	 *            the name of the object
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the size of the resolution grid.
	 * 
	 * @param size
	 *            the size of the resolution grid
	 */
	public void setSize(int size) {
		this.size = size;
	}
}
