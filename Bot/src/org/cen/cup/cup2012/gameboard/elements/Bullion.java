package org.cen.cup.cup2012.gameboard.elements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Point2D;

import org.cen.cup.cup2012.gameboard.Color2012;
import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.elements.util.DrawHelper;

public class Bullion extends AbstractGameBoardElement {

	public static final String NAME = "bullion";

	public static final double BULLION_WIDTH = 150d;

	public static final double BULLION_TOP_WIDTH = 124d;

	public static final double BULLION_HEIGHT = 70d;

	public static final double BULLION_TOP_HEIGHT = 44d;

	public Bullion(double x, double y, double orientation) {
		super(NAME, new Point2D.Double(x, y), orientation);
		order = 2;
	}

	@Override
	public void paint(Graphics2D g) {
		Stroke oldStroke = g.getStroke();
		g.setStroke(new BasicStroke(3));

		// RAL 1023
		Color color = Color2012.RAL_1023;
		g.setColor(color);
		DrawHelper.fillCenteredRectangle(g, 0, 0, BULLION_WIDTH, BULLION_HEIGHT);

		// Draw lines to see it as in 3d
		g.setColor(Color.BLACK);

		DrawHelper.drawCenteredRectangle(g, 0, 0, BULLION_TOP_WIDTH, BULLION_TOP_HEIGHT);

		// stroke at the left bottom
		g.drawLine((int) -BULLION_WIDTH / 2, (int) -BULLION_HEIGHT / 2, (int) -BULLION_TOP_WIDTH / 2,
				(int) -BULLION_TOP_HEIGHT / 2);

		// stroke at the left top
		g.drawLine((int) -BULLION_WIDTH / 2, (int) +BULLION_HEIGHT / 2, (int) -BULLION_TOP_WIDTH / 2,
				(int) +BULLION_TOP_HEIGHT / 2);

		// stroke at the right bottom
		g.drawLine((int) +BULLION_WIDTH / 2, (int) -BULLION_HEIGHT / 2, (int) +BULLION_TOP_WIDTH / 2,
				(int) -BULLION_TOP_HEIGHT / 2);

		// stroke at the right top
		g.drawLine((int) +BULLION_WIDTH / 2, (int) +BULLION_HEIGHT / 2, (int) +BULLION_TOP_WIDTH / 2,
				(int) +BULLION_TOP_HEIGHT / 2);

		g.setStroke(oldStroke);
	}

}
