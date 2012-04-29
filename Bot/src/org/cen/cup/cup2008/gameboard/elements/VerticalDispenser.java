package org.cen.cup.cup2008.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class VerticalDispenser extends AbstractGameBoardElement {
	public VerticalDispenser(String name, Point2D position, double orientation) {
		super(name, position, orientation);
		order = 9;

		Area a = new Area(new Rectangle2D.Double(0, -42.5, 42.5, 85));
		a.add(new Area(new Ellipse2D.Double(0, -42.5, 85, 85)));
		bounds = a;
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(Color.DARK_GRAY);
		g.draw(bounds);
	}
}
