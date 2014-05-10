package org.cen.cup.cup2010.ui.web;

import org.cen.geom.Point2D;
import java.util.Date;

import org.cen.robot.match.OpponentPosition;

/**
 * Wrapper of an opponent's position used to display the data in a view.
 * 
 * @author Emmanuel ZURMELY
 */
public class OpponentPositionView {
	private OpponentPosition position;

	private ElementsPositionsView view;

	/**
	 * Constructor.
	 * 
	 * @param position
	 *            the wrapped position
	 * @param view
	 *            the view backing bean
	 */
	public OpponentPositionView(OpponentPosition position, ElementsPositionsView view) {
		super();
		this.position = position;
		this.view = view;
	}

	public Date getDate() {
		return new Date(position.getTimestamp());
	}

	public double getDirection() {
		return Math.toDegrees(position.getDirection());
	}

	public Point2D getLocation() {
		return position.getLocation();
	}

	public OpponentPosition getMovement() {
		return position;
	}

	public void remove() {
		view.remove(this);
	}
}
