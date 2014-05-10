package org.cen.cup.cup2009.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import org.cen.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class Helper extends AbstractGameBoardElement {
	private static final Shape shape = new Rectangle2D.Double(-125, -7.5, 250,
			15);

	public Helper(Point2D position) {
		super("helper", position);
		order = 1;
	}

	@Override
	public void paint(Graphics2D g) {
		Graphics2D g2d = g;
		g2d.setColor(Color.BLACK);
		g2d.fill(shape);
	}
}
