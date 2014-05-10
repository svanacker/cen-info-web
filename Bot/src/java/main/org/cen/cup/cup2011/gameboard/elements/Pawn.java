package org.cen.cup.cup2011.gameboard.elements;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import org.cen.geom.Point2D;
import java.text.NumberFormat;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public class Pawn extends AbstractGameBoardElement {
	private static final int RADIUS_BASE = 100;

	public static final String NAME = "pawn";

	private final Color color;

	private final double score;

	private final NumberFormat format;

	public Pawn(Point2D position, double score) {
		this(position, score, Color.YELLOW);
	}

	public Pawn(Point2D position, double score, Color color) {
		super(NAME, position);
		this.score = score;
		this.color = color;
		order = 2;
		format = NumberFormat.getPercentInstance();
	}

	@Override
	public void paint(Graphics2D g) {
		int rgb = color.getRGB() & 0xFFFFFF;
		int alpha = (int) (score * 200) + 55;
		alpha <<= 24;
		g.setColor(new Color(rgb | alpha, true));
		g.fillOval(-RADIUS_BASE, -RADIUS_BASE, (RADIUS_BASE * 2),
				(RADIUS_BASE * 2));
	}

	@Override
	public void paintUnscaled(Graphics2D g) {
		Font f = g.getFont();
		Font newFont = f.deriveFont(10f);
		g.setFont(newFont);
		g.setColor(Color.BLACK);
		String s = format.format(score);
		g.drawString(s, -10, 0);
		g.setFont(f);
	}
}
