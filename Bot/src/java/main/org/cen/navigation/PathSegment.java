package org.cen.navigation;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a path segment in a location group. The path segment contains
 * control points for collecting object or doing actions. It can be enabled or
 * disabled depending on the availability of the control points.
 * 
 * @author Emmanuel ZURMELY
 */
public class PathSegment {
	private Set<ControlPoint> controlPoints = new HashSet<ControlPoint>();

	private Location start;

	private Location end;

	private boolean enabled = true;

	public PathSegment(Location start, Location end) {
		super();
		this.start = start;
		this.end = end;
	}

	public boolean contains(Location l) {
		return l == start || l == end;
	}

	private boolean controlPointsEnabled() {
		boolean b = true;
		for (ControlPoint cp : controlPoints) {
			b &= cp.isEnabled();
		}
		return b;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PathSegment) {
			return ((PathSegment) obj).isSameSegment(start, end);
		} else {
			return super.equals(obj);
		}
	}

	public Set<ControlPoint> getControlPoints() {
		return controlPoints;
	}

	public Location getEnd() {
		return end;
	}

	public Location getStart() {
		return start;
	}

	public boolean isEnabled() {
		return enabled && controlPointsEnabled();
	}

	public boolean isSameSegment(Location start, Location end) {
		return contains(start) && contains(end);
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[start=" + start + ", end=" + end + ", enabled=" + enabled + "]";
	}
}
