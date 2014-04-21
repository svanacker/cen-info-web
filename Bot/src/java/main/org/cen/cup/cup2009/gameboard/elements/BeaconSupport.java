package org.cen.cup.cup2009.gameboard.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class BeaconSupport extends AbstractGameBoardElement {

	public static final int BEACON_WIDTH = 80;

	public static final int BEACON_HEIGHT = 80;

	public BeaconSupport(String name, Point2D position) {
		super(name, position);
		order = 20;
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(-BEACON_WIDTH / 2, -BEACON_HEIGHT / 2, BEACON_WIDTH,
				BEACON_HEIGHT);
	}
}
