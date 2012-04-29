package org.cen.robot.device.collision.com;

import java.awt.geom.Point2D;

import org.cen.com.in.InData;

public class OpponentPositionInData extends InData {
	static final String HEADER = "/";

	private Point2D position;

	public OpponentPositionInData(Point2D position) {
		super();
		this.position = position;
	}

	public Point2D getPosition() {
		return position;
	}
}
