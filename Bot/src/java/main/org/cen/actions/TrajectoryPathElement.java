package org.cen.actions;

import java.awt.geom.Point2D;

public class TrajectoryPathElement {
	private Point2D start;

	private Point2D end;

	private boolean extendable = true;

	public TrajectoryPathElement(Point2D start) {
		super();
		this.start = start;
	}

	public Point2D getEnd() {
		return end;
	}

	public double getLength() {
		if (end != null) {
			return start.distance(end);
		} else {
			return 0;
		}
	}

	public Point2D getStart() {
		return start;
	}

	public boolean isExtendable() {
		return extendable;
	}

	public void setEnd(Point2D end) {
		this.end = end;
	}

	public void setExtendable(boolean extendable) {
		this.extendable = extendable;
	}

	public void setStart(Point2D start) {
		this.start = start;
	}
}
