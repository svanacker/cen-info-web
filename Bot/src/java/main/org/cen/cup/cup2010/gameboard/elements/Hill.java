package org.cen.cup.cup2010.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import org.cen.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class Hill extends AbstractGameBoardElement {
	public Hill(String name, Point2D position) {
		super(name, position);
		order = 2;
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 500, 1660);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 100, 500, 1460);
		g.setColor(Color.BLACK);
		g.drawRect(0, 580, 500, 500);
	}
}
