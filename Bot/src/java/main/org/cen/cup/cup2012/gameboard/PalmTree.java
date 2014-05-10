package org.cen.cup.cup2012.gameboard;

import java.awt.Graphics2D;
import org.cen.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.elements.util.DrawHelper;

public class PalmTree extends AbstractGameBoardElement {

	public static final double PALM_TREE_DIAMETER = 150d;

	public PalmTree(double x, double y) {
		super("palmTree", new Point2D.Double(x, y), 0.0d);
		order = 10;
	}

	@Override
	public void paint(Graphics2D g) {
		Graphics2D g2d = g;

		// Green
		g.setColor(Color2012.RAL_6018);
		DrawHelper.fillCenteredCircle(g2d, 0d, 0d, PALM_TREE_DIAMETER);

	}

}
