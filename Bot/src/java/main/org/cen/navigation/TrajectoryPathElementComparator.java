package org.cen.navigation;

import java.awt.geom.Point2D;
import java.util.Comparator;

public class TrajectoryPathElementComparator implements Comparator<TrajectoryPathElement> {
	private Point2D start;

	public TrajectoryPathElementComparator(Point2D start) {
		super();
		this.start = start;
	}

	@Override
	public int compare(TrajectoryPathElement o1, TrajectoryPathElement o2) {
		double d1 = o1.getPosition().distance(start);
		double d2 = o2.getPosition().distance(start);
		return Double.compare(d1, d2);
	}
}
