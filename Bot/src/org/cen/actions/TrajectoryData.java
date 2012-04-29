package org.cen.actions;

import java.awt.geom.Point2D;
import java.util.List;

import org.cen.navigation.Location;

public class TrajectoryData {
	public List<Location> locations;

	public int startIndex;

	public Point2D startPosition;

	public Point2D endPosition;

	public double orientation;

	public IGameAction endAction;

	public TrajectoryData(List<Location> locations, int startIndex, Point2D startPosition, Point2D endPosition, double orientation, IGameAction endAction) {
		super();
		this.locations = locations;
		this.startIndex = startIndex;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		this.orientation = orientation;
		this.endAction = endAction;
	}
}
