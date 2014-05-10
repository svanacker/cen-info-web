package org.cen.ui.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import org.cen.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class StartArea extends AbstractGameBoardElement {

	private final Color color;

	private final int widthSize;

	private final int heightSize;

	public StartArea(Color color, Point2D position, int widthSize,
			int heightSize) {
		super("start area", position);
		order = 1;
		this.color = color;
		this.widthSize = widthSize;
		this.heightSize = heightSize;
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(color);
		g.fillRect(0, 0, widthSize, heightSize);
	}
}
