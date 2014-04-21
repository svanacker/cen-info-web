package org.cen.cup.cup2009.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class VerticalDispenser extends AbstractGameBoardElement {
	public VerticalDispenser(String name, Point2D position) {
		super(name, position);
		order = 9;
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.drawOval(-40, -40, 80, 80);
	}
}
