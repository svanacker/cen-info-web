package org.cen.cup.cup2012.gameboard;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Point2D;

import org.cen.cup.cup2012.gameboard.elements.StartArea2012;
import org.cen.ui.gameboard.AbstractGameBoardElement;

public class CaptainBoatArea extends AbstractGameBoardElement {

	public static final int CAPTAIN_AREA_HEIGHT = 400;

	public CaptainBoatArea(double x, double y) {
		super("captainBoatArea", new Point2D.Double(x, y), 0.0d);
	}

	@Override
	public void paint(Graphics2D g2d) {

		// Brown
		g2d.setColor(Color2012.RAL_8002);

		// Captain Boat at the bottom
		int[] xPoints = new int[] { StartArea2012.START_AREA_WIDTH, StartArea2012.START_AREA_WIDTH,
				(int) GameBoard2012.BOARD_WIDTH, (int) GameBoard2012.BOARD_WIDTH };
		int[] yPoints = new int[] { 0, CAPTAIN_AREA_HEIGHT, 325, 0 };
		Polygon polygon = new Polygon(xPoints, yPoints, 4);

		g2d.fill(polygon);

		// Captain Boat at the top
		xPoints = new int[] { StartArea2012.START_AREA_WIDTH, StartArea2012.START_AREA_WIDTH,
				(int) GameBoard2012.BOARD_WIDTH, (int) GameBoard2012.BOARD_WIDTH };
		yPoints = new int[] { (int) GameBoard2012.BOARD_HEIGHT, (int) GameBoard2012.BOARD_HEIGHT - CAPTAIN_AREA_HEIGHT,
				(int) GameBoard2012.BOARD_HEIGHT - 325, (int) GameBoard2012.BOARD_HEIGHT };
		polygon = new Polygon(xPoints, yPoints, 4);

		g2d.fill(polygon);

	}
}