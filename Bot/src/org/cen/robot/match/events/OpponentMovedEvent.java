package org.cen.robot.match.events;

import java.awt.geom.Point2D;

import org.cen.robot.match.IMatchEvent;

public class OpponentMovedEvent implements IMatchEvent {
	private Point2D position;

	public OpponentMovedEvent(Point2D position) {
		super();
		this.position = position;
	}

	public Point2D getPosition() {
		return position;
	}
}
