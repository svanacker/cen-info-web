package org.cen.cup.cup2012.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import org.cen.geom.Point2D;

import org.cen.cup.cup2012.gameboard.Color2012;
import org.cen.ui.gameboard.AbstractGameBoardElement;
import org.cen.ui.gameboard.elements.util.DrawHelper;

public abstract class Coin extends AbstractGameBoardElement {

	public static final double COIN_DIAMETER = 120f;

	public static final double HOLE_DIAMETER = 20d;

	public Coin(String name, double x, double y) {
		super(name, new Point2D.Double(x, y), 0.0f);
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(getCoinColor());
		DrawHelper.fillCenteredCircle(g, 0d, 0d, COIN_DIAMETER);

		g.setColor(getHoleColor());
		DrawHelper.fillCenteredCircle(g, 0d, 0d, HOLE_DIAMETER);

		g.setColor(Color.BLACK);
		DrawHelper.drawCenteredCircle(g, 0d, 0d, COIN_DIAMETER);

	}

	public abstract Color getCoinColor();

	public Color getHoleColor() {
		return Color2012.RAL_5012;
	}
}
