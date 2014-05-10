package org.cen.cup.cup2012.gameboard.lines;

import java.awt.Graphics2D;
import org.cen.geom.Point2D;

import org.cen.cup.cup2012.gameboard.Color2012;
import org.cen.cup.cup2012.gameboard.GameBoard2012;
import org.cen.cup.cup2012.gameboard.elements.StartArea2012;
import org.cen.ui.gameboard.AbstractGameBoardElement;

public class FollowLine2012 extends AbstractGameBoardElement {

	public static final int LINE_WIDTH = 20;

	public static final int LINE_START_AREA_SHIFT = 50;

	public static final int LINE_VERTICAL_HEIGHT = 150;

	public FollowLine2012() {
		super("followLine2012", new Point2D.Double(0d, 0d), 0.0d);
	}

	@Override
	public void paint(Graphics2D g2d) {

		// Black
		g2d.setColor(Color2012.RAL_9005);

		int x = StartArea2012.START_AREA_WIDTH - LINE_START_AREA_SHIFT;

		// Bottom lines
		g2d.fillRect(x, StartArea2012.START_AREA_HEIGHT, LINE_WIDTH, LINE_VERTICAL_HEIGHT);
		g2d.fillRect(x, StartArea2012.START_AREA_HEIGHT + LINE_VERTICAL_HEIGHT - LINE_WIDTH,
				(int) GameBoard2012.BOARD_WIDTH - x, LINE_WIDTH);

		// Top lines
		g2d.fillRect(x, (int) (GameBoard2012.BOARD_HEIGHT - StartArea2012.START_AREA_HEIGHT - LINE_VERTICAL_HEIGHT),
				LINE_WIDTH, LINE_VERTICAL_HEIGHT);
		g2d.fillRect(x, (int) (GameBoard2012.BOARD_HEIGHT - StartArea2012.START_AREA_HEIGHT - LINE_VERTICAL_HEIGHT),
				(int) GameBoard2012.BOARD_WIDTH - x, LINE_WIDTH);
	}
}