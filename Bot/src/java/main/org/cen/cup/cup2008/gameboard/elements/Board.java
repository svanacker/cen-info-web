package org.cen.cup.cup2008.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class Board extends AbstractGameBoardElement {
	public Board(Point2D position) {
		super("board", position);
		order = 0;
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, 2100, 3000);
	}
}
