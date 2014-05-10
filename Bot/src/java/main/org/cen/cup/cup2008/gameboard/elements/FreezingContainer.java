package org.cen.cup.cup2008.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import org.cen.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class FreezingContainer extends AbstractGameBoardElement {
	public FreezingContainer(String name, Point2D position, double orientation) {
		super(name, position, orientation);
		order = 20;
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(-250, 0, 500, 250);
	}
}
