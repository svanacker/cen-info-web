package org.cen.cup.cup2012.gameboard.elements;

import java.awt.Color;

public class BlackCoin extends Coin {

	public BlackCoin(double x, double y) {
		super("blackCoin", x, y);
	}

	@Override
	public Color getCoinColor() {
		return Color.BLACK;
	}

}
