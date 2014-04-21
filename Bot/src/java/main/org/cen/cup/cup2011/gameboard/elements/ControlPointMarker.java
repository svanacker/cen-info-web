package org.cen.cup.cup2011.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class ControlPointMarker extends AbstractGameBoardElement {
	public ControlPointMarker(String name, Point2D position) {
		super(name, position);
		order = 10;
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillOval(-10, -10, 20, 20);
	}
}
