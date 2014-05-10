package org.cen.cup.cup2012.gameboard.elements;

import java.awt.Color;
import org.cen.geom.Point2D;

import org.cen.cup.cup2012.gameboard.Color2012;
import org.cen.ui.gameboard.elements.StartArea;

public class StartArea2012 extends StartArea {

	public static final int START_AREA_WIDTH = 500;

	public static final int START_AREA_HEIGHT = 500;

	public static final Color PURPLE = Color2012.RAL_4008;

	public static final Color RED = Color2012.RAL_3001;

	public StartArea2012(Color color, double x, double y) {
		this(color, new Point2D.Double(x, y));
	}

	public StartArea2012(Color color, Point2D position) {
		super(color, position, START_AREA_WIDTH, START_AREA_HEIGHT);
	}
}
