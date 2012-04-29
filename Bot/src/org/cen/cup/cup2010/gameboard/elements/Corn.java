package org.cen.cup.cup2010.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class Corn extends AbstractGameBoardElement {
	private static final int RADIUS = 25;

	private final boolean fixed;

	public Corn(String name, Point2D position, boolean fixed) {
		super(name, position);
		this.fixed = fixed;
		order = (fixed) ? 3 : 2;
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.drawOval(-RADIUS, -RADIUS, (RADIUS * 2), (RADIUS * 2));
		g.setColor((fixed) ? Paint.RAL_9017 : Paint.RAL_1013);
		g.fillOval(-RADIUS, -RADIUS, (RADIUS * 2), (RADIUS * 2));
	}
}
