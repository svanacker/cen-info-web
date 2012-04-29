package org.cen.cup.cup2009.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class Border extends AbstractGameBoardElement {
	private final Color color;

	private final double length;

	private final double width;

	public Border(String name, double length, double width, Color color,
			Point2D position, double orientation) {
		super(name, position, orientation);
		order = 5;
		this.length = length;
		this.color = color;
		this.width = width;
	}

	public Border(String name, double length, Point2D position,
			double orientation) {
		this(name, length, 22, Color.BLACK, position, orientation);
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(color);
		g.fillRect(0, 0, (int) length, (int) width);
	}
}
