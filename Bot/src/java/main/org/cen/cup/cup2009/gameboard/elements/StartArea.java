package org.cen.cup.cup2009.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class StartArea extends AbstractGameBoardElement {

	public static final int START_AREA_WIDTH = 500;

	public static final int START_AREA_HEIGHT = 500;

	public static enum StartAreaColor {
		GREEN, RED;
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
		case GREEN:
			c = Paint.RAL_6018;
			break;
		case RED:
			c = Paint.RAL_3020;
			break;
		default:
			return;
		}
		g.setColor(c);
		g.fillRect(0, 0, START_AREA_WIDTH, START_AREA_HEIGHT);
	}
}
