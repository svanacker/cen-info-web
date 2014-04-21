package org.cen.cup.cup2009.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class ColumnElement extends AbstractGameBoardElement {
	public static enum ColumnElementColor {
		GREEN, NEUTRAL, RED;
	}

	private ColumnElementColor color;

	public ColumnElement(String name, ColumnElementColor color, Point2D position) {
		super(name, position);
		this.color = color;
		order = 3;
	}

	@Override
	public void paint(Graphics2D g) {
		Color c;
		switch (color) {
		case GREEN:
			c = Paint.RAL_6018;
			break;
		case RED:
			c = Paint.RAL_3020;
			break;
		case NEUTRAL:
			c = Color.WHITE;
			break;
		default:
			return;
		}
		g.setColor(c);
		g.fillOval(-40, -40, 80, 80);
	}

	public void setColor(ColumnElementColor color) {
		this.color = color;
	}

	public void setPosition(Point2D position) {
		this.position.setLocation(position);
	}
}
