package org.cen.cup.cup2008.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import org.cen.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class HorizontalDispenser extends AbstractGameBoardElement {
	public HorizontalDispenser(String name, Point2D position) {
		super(name, position);
		order = 9;
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, 80, 910);
	}
}
