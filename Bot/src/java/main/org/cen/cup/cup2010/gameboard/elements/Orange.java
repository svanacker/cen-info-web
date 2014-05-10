package org.cen.cup.cup2010.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import org.cen.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class Orange extends AbstractGameBoardElement {
	private static final int RADIUS = 50;

	public Orange(String name, Point2D position) {
		super(name, position);
		order = 2;
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(Color.ORANGE);
		g.fillOval(-RADIUS, -RADIUS, (RADIUS * 2), (RADIUS * 2));
	}
}
