package org.cen.cup.cup2012.gameboard;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import org.cen.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class ShipHold extends AbstractGameBoardElement {

	public static final int SHIP_HOLD_WIDTH = 610;

	public static final int SHIP_HOLD_HEIGHT = 340;

	public static final int SHIP_PROTECTION_WIDTH = 750;

	public ShipHold(double x, double y) {
		super("shipHold", new Point2D.Double(x, y), 0.0d);
	}

	@Override
	public void paint(Graphics2D g) {

		g.setColor(Color.GRAY);
		Shape rectangle = new Rectangle2D.Double(0, 0, SHIP_HOLD_WIDTH, SHIP_HOLD_HEIGHT);
		g.fill(rectangle);

		// Ship Hold Protection
		g.setColor(Color.BLACK);
		int x1 = SHIP_HOLD_WIDTH - SHIP_PROTECTION_WIDTH;
		int x2 = SHIP_HOLD_WIDTH;
		int[] xPoints = new int[] { x1, x1 + 2, x2, x2 };

		int y1 = SHIP_HOLD_HEIGHT + 20;
		int y2 = SHIP_HOLD_HEIGHT;
		int protectionHeight = 22;
		int[] yPoints = new int[] { y1, y1 + protectionHeight, y2, y2 - protectionHeight };
		Polygon polygon = new Polygon(xPoints, yPoints, 4);

		g.fill(polygon);
	}

}