package org.cen.cup.cup2008.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import org.cen.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class StandardContainer extends AbstractGameBoardElement {
	public StandardContainer(String name, Point2D position) {
		super(name, position);
		order = 3;
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(Color.CYAN);
		g.fillRect(0, 0, 80, 3044);
		g.setColor(Color.BLUE);
		g.fillRect(0, 1520, 80, 4);
	}
}
