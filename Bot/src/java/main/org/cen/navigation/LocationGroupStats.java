package org.cen.navigation;

import java.util.List;

/**
 * Statistics resulting from a group analysis.
 * 
 * @author Emmanuel ZURMELY
 */
public class LocationGroupStats {
	private LocationGroup group;

	private int cost;

	private int interest;

	private List<Location> path;

	/**
	 * Constructor.
	 * 
	 * @param group
	 *            the location group
	 * @param cost
	 *            the global cost of the group
	 * @param interest
	 *            the global interest of the group
	 */
	public LocationGroupStats(LocationGroup group, int cost, int interest, List<Location> path) {
		super();
		this.group = group;
		this.cost = cost;
		this.interest = interest;
		this.path = path;
	}

	/**
	 * Returns the global cost of the group.
	 * 
	 * @return the global cost of the group
	 */
	public int getCost() {
		return cost;
	}

	/**
	 * Returns the location group targeted by this statistics.
	 * 
	 * @return the location group targeted by this statistics
	 */
	public LocationGroup getGroup() {
		return group;
	}

	/**
	 * Returns the global interest of this group.
	 * 
	 * @return the global interest of this group
	 */
	public int getInterest() {
		return interest;
	}

	/**
	 * Returns the path to the locations group.
	 * 
	 * @return the path to the locations group
	 */
	public List<Location> getPath() {
		return path;
	}

	@Override
	public String toString() {
		return "LocationGroupStats [group name=" + group.getName() + ", cost=" + cost + ", interest=" + interest + "]";
	}
}
