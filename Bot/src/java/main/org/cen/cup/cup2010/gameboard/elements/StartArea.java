package org.cen.cup.cup2010.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class StartArea extends AbstractGameBoardElement {
	public static final int START_AREA_WIDTH = 500;

	public static final int START_AREA_HEIGHT = 500;

	public static enum StartAreaColor {
		YELLOW, BLUE;
	}

	private final StartAreaColor color;

	public StartArea(StartAreaColor color, Point2D position) {
		super("start area", position);
		order = 1;
		this.color = color;
	}

	@Override
	public void paint(Graphics2D g) {
		Color c;
		switch (color) {
		case YELLOW:
			c = Paint.RAL_1023;
			break;
		case BLUE:
			c = Paint.RAL_5005;
			break;
		default:
			return;
		}
		g.setColor(c);
		g.fillRect(0, 0, START_AREA_WIDTH, START_AREA_HEIGHT);
	}
}
