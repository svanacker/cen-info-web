package org.cen.cup.cup2011.actions;

import org.cen.geom.Point2D;

import org.cen.actions.AbstractGameAction;
import org.cen.actions.IGameActionHandler;
import org.cen.actions.TrajectoryPathElement;

public abstract class AbstractPickerAction extends AbstractGameAction {
	@Override
	public Point2D getActingPosition(IGameActionHandler handler, TrajectoryPathElement pathElement) {
		if (handler instanceof Picker2011) {
			// Ajuste le point d'arrivée sur la position de la pince
			Point2D p = handler.getPositionOnRobot();
			Point2D result = getPositionFromPathEnd(pathElement, p);
			return result;
		}
		return null;
	}
}
