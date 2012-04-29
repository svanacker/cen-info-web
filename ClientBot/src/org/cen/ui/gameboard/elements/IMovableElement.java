package org.cen.ui.gameboard.elements;

import java.awt.Shape;
import java.awt.geom.Point2D;

import org.cen.ui.gameboard.IGameBoardElement;

public interface IMovableElement extends IGameBoardElement {

	public abstract Shape getAbsoluteBounds();

	@Override
	public abstract boolean isObstacle();

	public abstract Boolean isPickable();

	public abstract void setOrientation(double orientation);

	public abstract void setPickable(Boolean isPickable);

	public abstract void setPosition(Point2D position);

}