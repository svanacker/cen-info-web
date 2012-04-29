package org.cen.cup.cup2011.simulGameboard.elements;

import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.elements.AMovableElement;
public class SimulPawnElement extends AMovableElement {

	public static final String NAME = "pawn";

	private static final int RADIUS_BASE = 100;


	public SimulPawnElement(Point2D position, double orientation) {
		super(NAME, position, orientation);
		bounds = new Ellipse2D.Float(-RADIUS_BASE, -RADIUS_BASE, (RADIUS_BASE * 2), (RADIUS_BASE * 2));
		setPickable(true);
		int rgb = Color.YELLOW.getRGB();
		color = new Color(rgb, true);
	}
}
