package org.cen.cup.cup2011.actions;

import org.cen.geom.Point2D;

import org.cen.actions.AbstractGameActionHandler;

public class Picker2011 extends AbstractGameActionHandler {
	private static final Point2D POSITION = new Point2D.Double(0, 120);

	@Override
	public String getDescription() {
		return "pince 2011";
	}

	@Override
	public Point2D getPositionOnRobot() {
		return POSITION;
	}
}
