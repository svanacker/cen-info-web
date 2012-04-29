package org.cen.navigation;

import java.util.HashSet;
import java.util.Set;

/**
 * Group of locations on the gameboard.
 * 
 * @author Emmanuel ZURMELY
 */
public class LocationGroup {
	protected Set<PathSegment> segments = new HashSet<PathSegment>();

	private String name;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            the name of this group
	 */
	public LocationGroup(String name) {
		super();
		this.name = name;
	}

	/**
	 * Returns all the control points contained in this group.
	 * 
	 * @return the control points contained in this group
	 */
	public Set<ControlPoint> getControlPoints() {
		Set<ControlPoint> controlPoints = new HashSet<ControlPoint>();
		for (PathSegment s : segments) {
			controlPoints.addAll(s.getControlPoints());
		}
		return controlPoints;
	}

	/**
	 * Returns the interest score of this group.
	 * 
	 * @return the interest score of this group
	 */
	public int getInterest() {
		return 0;
	}

	/**
	 * Returns the locations in this group.
	 * 
	 * @return the locations in this group
	 */
	public Set<Location> getLocations() {
		Set<Location> locations = new HashSet<Location>();
		for (PathSegment s : segments) {
			locations.add(s.getStart());
			locations.add(s.getEnd());
		}
		return locations;
	}

	/**
	 * Returns the name of this group.
	 * 
	 * @return the name of this group
	 */
	public String getName() {
		return name;
	}

	public Set<PathSegment> getSegments() {
		return segments;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[name=" + name + "]";
	}
}
