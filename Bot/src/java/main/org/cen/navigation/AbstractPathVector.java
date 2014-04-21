package org.cen.navigation;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractPathVector implements IPathVector {

	protected final Location l1;

	protected final Location l2;

	public AbstractPathVector(Location l1, Location l2) {
		super();
		this.l1 = l1;
		this.l2 = l2;
	}

	@Override
	public boolean containsLocation(Location l) {
		return l == l1 || l == l2;
	}

	@Override
	public Location getOtherEnd(Location l) {
		if (l == l1) {
			return l2;
		} else if (l == l2) {
			return l1;
		} else {
			return null;
		}
	}

	@Override
	public Location getStart() {
		return l1;
	}

	@Override
	public Location getEnd() {
		return l2;
	}

	@Override
	public Collection<Location> getLocations() {
		Collection<Location> locations = new ArrayList<Location>();
		locations.add(getStart());
		locations.add(getEnd());
		return locations;
	}
}
