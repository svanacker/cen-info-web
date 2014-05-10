package org.cen.cup.cup2008.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import org.cen.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class StartArea extends AbstractGameBoardElement {
	public static enum StartAreaColor {
		BLUE, RED;
	}

	private final StartAreaColor color;

	public StartArea(StartAreaColor color, Point2D position) {
		super("start area", position);
		order = 1;
		this.color = color;
	}

	@Override
	public void paint(Graphics2D g) {
		Color c;
		switch (color) {
		case BLUE:
			c = Color.BLUE;
			break;
		case RED:
			c = Color.RED;
			break;
		default:
			return;
		}
		g.setColor(c);
		g.fillRect(0, 0, 500, 500);
	}
}
