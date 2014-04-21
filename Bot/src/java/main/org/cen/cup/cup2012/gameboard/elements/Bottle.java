package org.cen.cup.cup2012.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.elements.util.DrawHelper;

public class Bottle extends AbstractGameBoardElement {

	public static final int BOTTLE_WIDTH = 200;

	// NOT GOOD VALUE
	public static final int BOTTLE_DIAMETER = 50;

	private final Color color;

	public Bottle(String name, double x, double y, Color color) {
		super(name, new Point2D.Double(x, y), 0.0d);
		this.color = color;
	}

	@Override
	public void paint(Graphics2D g2d) {

		// Green
		g2d.setColor(color);
		DrawHelper.fillCenteredRectangle(g2d, 0d, 0d, BOTTLE_DIAMETER, BOTTLE_WIDTH);
	}

}
