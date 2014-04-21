package org.cen.navigation;

import java.awt.geom.Point2D;

public class TrajectoryPathElement {
	private Point2D position;

	private ControlPoint controlPoint;

	private Location segmentStart;

	private Location segmentEnd;

	public TrajectoryPathElement(Point2D position, ControlPoint controlPoint, Location segmentStart, Location segmentEnd) {
		super();
		this.position = position;
		this.controlPoint = controlPoint;
		this.segmentStart = segmentStart;
		this.segmentEnd = segmentEnd;
	}

	public ControlPoint getControlPoint() {
		return controlPoint;
	}

	public Point2D getPosition() {
		return position;
	}

	public Location getSegmentEnd() {
		return segmentEnd;
	}

	public Location getSegmentStart() {
		return segmentStart;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[position=" + position + "]";
	}
}
