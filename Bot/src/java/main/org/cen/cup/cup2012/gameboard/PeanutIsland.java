package org.cen.cup.cup2012.gameboard;

import java.awt.Graphics2D;
import java.awt.Shape;
import org.cen.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.elements.util.DrawHelper;

public class PeanutIsland extends AbstractGameBoardElement {

	private static final double GREEN_AREA_DIAMETER = 400d;

	private static final double YELLOW_AREA_DIAMETER = 600d;

	private static final double CIRCLE_DISTANCE = 400d;

	private static final double FAKE_CIRCLE_DIAMETER = 1100d;

	public PeanutIsland(double x, double y) {
		super("peanutIsland", new Point2D.Double(x, y), 0.0d);
	}

	@Override
	public void paint(Graphics2D g) {
		Shape clip = g.getClip();
		// Yellow
		g.setColor(Color2012.RAL_1023);

		DrawHelper.fillCenteredCircle(g, 0d, -CIRCLE_DISTANCE, YELLOW_AREA_DIAMETER);
		DrawHelper.fillCenteredCircle(g, 0d, CIRCLE_DISTANCE, YELLOW_AREA_DIAMETER);

		DrawHelper.fillCenteredRectangle(g, YELLOW_AREA_DIAMETER - 100d, CIRCLE_DISTANCE * 2 - 200d);

		// Green
		g.setColor(Color2012.RAL_6018);
		DrawHelper.fillCenteredCircle(g, 0d, -CIRCLE_DISTANCE, GREEN_AREA_DIAMETER);
		DrawHelper.fillCenteredCircle(g, 0d, CIRCLE_DISTANCE, GREEN_AREA_DIAMETER);

		// Link in yellow between zone
		g.clipRect((int) -YELLOW_AREA_DIAMETER / 2, (int) -CIRCLE_DISTANCE, (int) YELLOW_AREA_DIAMETER,
				(int) CIRCLE_DISTANCE * 2);
		g.setColor(Color2012.RAL_5012);
		DrawHelper.fillCenteredCircle(g, -GREEN_AREA_DIAMETER / 2 - FAKE_CIRCLE_DIAMETER / 2, 0d, FAKE_CIRCLE_DIAMETER);
		DrawHelper.fillCenteredCircle(g, +GREEN_AREA_DIAMETER / 2 + FAKE_CIRCLE_DIAMETER / 2, 0d, FAKE_CIRCLE_DIAMETER);
		g.setClip(clip);
	}
}