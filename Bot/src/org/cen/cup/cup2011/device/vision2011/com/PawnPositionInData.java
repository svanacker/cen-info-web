package org.cen.cup.cup2011.device.vision2011.com;

import java.awt.geom.Point2D;

import org.cen.com.in.InData;

public class PawnPositionInData extends InData {
	public static final String HEADER = "µ";

	private static final Point2D finalPosition = new Point2D.Double(Double.NaN, Double.NaN);

	private Point2D position;

	public PawnPositionInData(int x, int y) {
		super();
		if (x == -1 || y == -1) {
			position = finalPosition;
		} else {
			position = new Point2D.Double(x, y);
		}
	}

	public Point2D getPosition() {
		return position;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[position=" + position + "]";
	}
}
