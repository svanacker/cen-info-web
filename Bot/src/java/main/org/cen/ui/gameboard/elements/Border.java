package org.cen.ui.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.cen.cup.cup2011.gameboard.GameBoard2011;
import org.cen.ui.gameboard.AbstractGameBoardElement;

public class Border extends AbstractGameBoardElement {

	private final Color color;

	private final double length;

	private final double width;

	public Border(String name, double length, double width, Color color,
			Point2D position, double orientation) {
		super(name, position, orientation);
		order = 6;
		this.length = length;
		this.color = color;
		this.width = width;
	}

	public Border(String name, double length, Point2D position,
			double orientation) {
		this(name, length, GameBoard2011.BORDER_WIDTH, Color.BLACK, position,
				orientation);
	}

	public Border(String name, double length, double x, double y,
			double orientation) {
		this(name, length, GameBoard2011.BORDER_WIDTH, Color.BLACK,
				new Point2D.Double(x, y), orientation);
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(color);
		g.fillRect(0, 0, (int) length, (int) width);
	}
}
