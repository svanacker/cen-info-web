package org.cen.cup.cup2009.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.cen.cup.cup2009.gameboard.elements.ColumnElement.ColumnElementColor;
import org.cen.ui.gameboard.AbstractGameBoardElement;

public class ColumnElementPosition extends AbstractGameBoardElement {
	private final ColumnElementColor color;

	public ColumnElementPosition(int positionId, ColumnElementColor color,
			Point2D position) {
		super("column element position " + positionId, position);
		this.color = color;
		order = 1;
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
		g.drawOval(-35, -35, 70, 70);
	}
}
