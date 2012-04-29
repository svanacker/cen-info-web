package org.cen.ui.gameboard.elements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

public abstract class AMovableElement extends AbstractGameBoardElement implements IMovableElement {

	protected Color color = new Color(0, 0, 0, 0);

	protected boolean obstacle = false;

	protected Boolean pickable = false;

	protected Stroke stroke = new BasicStroke();

	public AMovableElement(String NAME, Point2D position, double orientation) {
		super(NAME, position, orientation);
		order = 2;
		bounds = new Rectangle();
	}

	@Override
	public Shape getAbsoluteBounds() {
		AffineTransform transform = new AffineTransform();
		transform.translate(getPosition().getX(), getPosition().getY());
		transform.rotate(getOrientation());
		return transform.createTransformedShape(bounds);
	}

	@Override
	public boolean isObstacle() {
		return obstacle ;
	}

	@Override
	public Boolean isPickable() {
		return pickable;
	}

	@Override
	public void paint(Graphics2D g) {
		Graphics2D g2d = (Graphics2D)g;
		g.setColor(color);
		g2d.fill(bounds);
	}

	@Override
	public void setOrientation(double orientation) {
		this.orientation = orientation;
	}

	@Override
	public void setPickable(Boolean isPickable) {
		this.pickable = isPickable;
	}

	@Override
	public void setPosition(Point2D position) {
		this.position = position;
	}
}
