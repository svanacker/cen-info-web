package org.cen.cup.cup2008.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.cen.cup.cup2008.gameboard.elements.Ball.BallColor;
import org.cen.ui.gameboard.AbstractGameBoardElement;

public class BallPosition extends AbstractGameBoardElement {
	private final BallColor color;

	public BallPosition(String name, BallColor color, Point2D position) {
		super(name, position);
		order = 9;
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
		case WHITE:
			c = Color.WHITE;
			break;
		default:
			return;
		}
		g.setColor(c);
		g.drawOval(-36, -36, 72, 72);
	}
}
