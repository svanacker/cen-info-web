package org.cen.cup.cup2011.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class Circle extends AbstractGameBoardElement {

	private final int radius;
	private final Color color;

	public Circle(String name, Integer radius, Color color, Point2D position) {
		super(name, position);
		this.radius = radius;
		this.color = color;
		order = 5;
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(color);
		g.fillOval(-radius, -radius, (radius * 2), (radius * 2));
	}
}
