package org.cen.vision.util;

import java.awt.Point;
import org.cen.geom.Point2D;
import java.util.Collections;

import org.cen.vision.Acquisition;
import org.cen.vision.filters.TargetStat.TargetLocation;

public class GoalTargetHandler extends TargetHandler {
	protected double goalDetectionThreshold = 1.5;

	private double topBound = 0;

	private double bottomBound = 1d;

	public GoalTargetHandler(Acquisition acquisition) {
		super(acquisition);
	}

	@Override
	protected void analyze() {
		bestTargets.clear();
		int ly = (int) (historyGrid[0].length * topBound);
		int hy = (int) (historyGrid[0].length * bottomBound);

		for (int x = 0; x < historyGrid.length; x++) {
			double max = 0;
			int mx = 0, my = 0;
			for (int y = ly; y < hy; y++) {
				if (historyGrid[x][y] > max) {
					max = historyGrid[x][y];
					mx = x;
					my = y;
				}
			}

			Point location = new Point(mx, my);
			// Game board coordinates
			if (transform != null) {
				// Screen coordinates
				// Target location in screen coordinates
				Point2D position = transform.screenToGameBoard(location.x, location.y);
				if (max >= weightThreshold)
					notifyTarget(position, max);
				else if (tries++ > maxTryCount)
					notifyFailure();
			}
			TargetLocation target = new TargetLocation(location, max);
			bestTargets.add(target);
		}
		Collections.sort(bestTargets);
	}

	public double getGoadDetectionThreshold() {
		return goalDetectionThreshold;
	}

	public TargetLocation[] getGoalBounds() {
		TargetLocation[] locations = new TargetLocation[2];
		int ileft = historyGrid.length - 1, iright = 0;
		int left = 320, right = 0;
		for (int i = 0; i < bestTargets.size(); i++) {
			TargetLocation l = bestTargets.get(i);
			if (l.getWeight() < goalDetectionThreshold) {
				continue;
			}
			Point location = l.getLocation();
			if (location.x > right) {
				right = location.x;
				iright = i;
			}
			if (location.x < left) {
				left = location.x;
				ileft = i;
			}
		}
		locations[0] = bestTargets.get(ileft);
		locations[1] = bestTargets.get(iright);
		return locations;
	}

	public void setGoadDetectionThreshold(double goadDetectionThreshold) {
		this.goalDetectionThreshold = goadDetectionThreshold;
	}

	public double getTopBound() {
		return topBound;
	}

	public void setTopBound(double topBound) {
		this.topBound = topBound;
	}

	public double getBottomBound() {
		return bottomBound;
	}

	public void setBottomBound(double bottomBound) {
		this.bottomBound = bottomBound;
	}
}
