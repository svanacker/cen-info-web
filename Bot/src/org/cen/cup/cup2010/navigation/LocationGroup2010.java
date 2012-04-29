package org.cen.cup.cup2010.navigation;

import org.cen.navigation.ControlPoint;
import org.cen.navigation.LocationGroup;

public class LocationGroup2010 extends LocationGroup {
	private int goalProximity;

	public LocationGroup2010(String name, int goalProximity) {
		super(name);
		this.goalProximity = goalProximity;
	}

	private int[] getControlPointCounts() {
		int tomatoCount = 0;
		int cornCount = 0;
		for (ControlPoint c : getControlPoints()) {
			if (c instanceof CornControlPoint2010) {
				CornControlPoint2010 t = (CornControlPoint2010) c;
				if (t.isEnabled()) {
					cornCount++;
				}
			} else if (c instanceof TomatoControlPoint2010) {
				TomatoControlPoint2010 t = (TomatoControlPoint2010) c;
				if (t.isEnabled()) {
					tomatoCount++;
				}
			}
		}
		return new int[] { tomatoCount, cornCount };

	}

	public int getCornCount() {
		return getControlPointCounts()[1];
	}

	public int getGoalProximity() {
		return goalProximity;
	}

	@Override
	public int getInterest() {
		int[] counts = getControlPointCounts();
		return counts[0] + counts[1] + goalProximity;
	}

	public int getTomatoCount() {
		return getControlPointCounts()[0];
	}
}
