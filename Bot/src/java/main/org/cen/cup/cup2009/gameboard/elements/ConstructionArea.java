package org.cen.cup.cup2009.gameboard.elements;

import java.awt.Graphics2D;
import org.cen.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class ConstructionArea extends AbstractGameBoardElement {
	public ConstructionArea(String name, Point2D position) {
		super(name, position);
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(Paint.RAL_8017);
		g.fillRect(-50, -300, 100, 600);
	}
}
