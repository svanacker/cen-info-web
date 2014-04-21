package org.cen.cup.cup2012.gameboard.elements;

import java.awt.Color;

public class WhiteCoin extends Coin {

	public WhiteCoin(double x, double y) {
		super("whiteCoin", x, y);
	}

	@Override
	public Color getCoinColor() {
		return Color.WHITE;
	}

}
