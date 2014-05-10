package org.cen.cup.cup2011.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import org.cen.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class King extends AbstractGameBoardElement {
	private static final int RADIUS_BASE = 200;

	public King(String name, Point2D position) {
		super(name, position);
		order = 2;
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(Color.YELLOW);
		g.fillOval(-RADIUS_BASE, -RADIUS_BASE, (RADIUS_BASE * 2),
				(RADIUS_BASE * 2));
	}
}
