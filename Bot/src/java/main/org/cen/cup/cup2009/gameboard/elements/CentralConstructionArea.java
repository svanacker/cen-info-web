package org.cen.cup.cup2009.gameboard.elements;

import java.awt.Graphics2D;
import org.cen.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class CentralConstructionArea extends AbstractGameBoardElement {
	public CentralConstructionArea(String name, Point2D position) {
		super(name, position);
		order = 1;
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(Paint.RAL_8017);
		g.fillOval(-150, -150, 300, 300);
	}
}
