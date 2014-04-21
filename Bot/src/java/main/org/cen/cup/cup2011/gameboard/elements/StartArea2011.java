package org.cen.cup.cup2011.gameboard.elements;

import java.awt.Color;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.elements.StartArea;

public class StartArea2011 extends StartArea {

	public static final int START_AREA_HEIGHT = 400;

	public static final int START_AREA_WIDTH = 400;

	public StartArea2011(Color color, Point2D position) {
		super(color, position, START_AREA_WIDTH, START_AREA_HEIGHT);
	}

}
