package org.cen.ui.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import org.cen.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class Board extends AbstractGameBoardElement {

	private final Color color;

	private final double width;

	private final double height;

	public Board(Color color, double width, double height) {
		super("board", new Point2D.Double(0d, 0d));
		this.width = width;
		this.height = height;
		this.color = color;
		order = 0;
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(color);
		g.fillRect(0, 0, (int) width, (int) height);
	}
}
