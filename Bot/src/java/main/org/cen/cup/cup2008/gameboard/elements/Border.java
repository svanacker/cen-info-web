package org.cen.cup.cup2008.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import org.cen.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class Border extends AbstractGameBoardElement {

	private final double length;

	public Border(String name, double length, Point2D position,
			double orientation) {
		super(name, position, orientation);
		order = 2;
		this.length = length;
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, (int) length, 22);
	}
}
