package org.cen.cup.cup2012.gameboard;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.elements.util.DrawHelper;

public class MapIsland extends AbstractGameBoardElement {

	public static final double GREEN_AREA_DIAMETER = 600d;

	public static final double YELLOW_AREA_DIAMETER = 800d;

	public static final double MAP_HEIGHT = 200d;

	public MapIsland(double x, double y) {
		super("mapIsland", new Point2D.Double(x, y), 0.0d);
	}

	@Override
	public void paint(Graphics2D g) {
		// save original clip
		Shape clip = g.getClip();

		// Clip zone
		g.clipRect(0, -(int) YELLOW_AREA_DIAMETER / 2, (int) GameBoard2012.BOARD_WIDTH, (int) YELLOW_AREA_DIAMETER);

		// Yellow
		g.setColor(Color2012.RAL_1023);
		DrawHelper.fillCenteredCircle(g, YELLOW_AREA_DIAMETER);

		// Green
		g.setColor(Color2012.RAL_6018);
		DrawHelper.fillCenteredCircle(g, GREEN_AREA_DIAMETER);

		g.setClip(clip);

		// Map
		g.setColor(Color.GRAY);
		double width = 200d;
		double height = MAP_HEIGHT * 2;
		DrawHelper.fillCenteredRectangle(g, -width / 2, 0, width, height);

		g.setColor(Color.BLACK);
		int stroke = 20;
		g.fillRect((int) -width, (int) (-height / 2), (int) width, stroke);
		g.fillRect((int) -width, (int) (+height / 2), (int) width, stroke);

		DrawHelper.fillCenteredRectangle(g, 10d, 0d, stroke, stroke);
		DrawHelper.fillCenteredRectangle(g, 10d - width / 2, 0d, stroke, stroke);
	}

}