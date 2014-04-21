package org.cen.navigation;

import java.util.Comparator;

public class LocationGroupStatsComparator implements Comparator<LocationGroupStats> {
	@Override
	public int compare(LocationGroupStats o1, LocationGroupStats o2) {
		return (o2.getInterest() - o2.getCost()) - (o1.getInterest() - o1.getCost());
	}
}
