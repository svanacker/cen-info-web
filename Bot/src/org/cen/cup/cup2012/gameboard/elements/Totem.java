package org.cen.cup.cup2012.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.elements.util.DrawHelper;

public class Totem extends AbstractGameBoardElement {

	public static final String NAME = "totem";

	public static final double TOTEM_WIDTH = 250;

	public static final double TOTEM_INTERNAL_WIDTH = 70;

	public static final double TOTEM_HEIGHT = 250;

	public static final double TOTEM_TOP_HEIGHT = 70;

	// RAL 8002
	public static final Color TOTEM_COLOR = new Color(110, 59, 58);

	public Totem(double x, double y) {
		super(NAME, new Point2D.Double(x, y));
	}

	@Override
	public void paint(Graphics2D g) {
		Graphics2D g2d = g;

		g.setColor(TOTEM_COLOR);

		// Draw external rectangle
		DrawHelper.fillCenteredRectangle(g2d, 0d, 0d, TOTEM_WIDTH, TOTEM_HEIGHT);
	}

}
