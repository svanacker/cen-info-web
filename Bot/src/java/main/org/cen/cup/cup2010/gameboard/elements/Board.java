package org.cen.cup.cup2010.gameboard.elements;

import java.awt.Graphics2D;
import org.cen.geom.Point2D;

import org.cen.cup.cup2010.gameboard.GameBoard2010;
import org.cen.ui.gameboard.AbstractGameBoardElement;

public class Board extends AbstractGameBoardElement {
	public Board(Point2D position) {
		super("board", position);
		order = 0;
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(Paint.RAL_6018);
		g.fillRect(0, 0, GameBoard2010.BOARD_WIDTH, GameBoard2010.BOARD_HEIGHT);
	}
}
