package org.cen.cup.cup2008.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class Beacon extends AbstractGameBoardElement {

	public Beacon(String name, Point2D position) {
		super(name, position);
		order = 20;
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(-40, -40, 80, 80);
	}
}
