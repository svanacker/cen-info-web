package org.cen.cup.cup2012.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class TreasuryMap extends AbstractGameBoardElement {

	private final Color color;

	public TreasuryMap(String name, double x, double y, Color color) {
		super(name, new Point2D.Double(x, y), 0.0d);
		this.color = color;
	}

	@Override
	public void paint(Graphics2D g) {
		Graphics2D g2d = g;
		//
		// // Green
		// g.setColor(color);
		// DrawHelper.fillCenteredRectangle(g2d, position.getX(),
		// position.getY(),
		// BOTTLE_DIAMETER, BOTTLE_WIDTH);
	}

}
