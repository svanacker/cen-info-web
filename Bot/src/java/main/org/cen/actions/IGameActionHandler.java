package org.cen.actions;

import java.awt.geom.Point2D;

/**
 * Interface of an action handler. An action handler can handle an IGameAction
 * object.
 * 
 * @author Emmanuel ZURMELY
 */
public interface IGameActionHandler {
	/**
	 * Returns the position of the action handler on the robot
	 * 
	 * @return the position of the action handler relative to the robot
	 */
	public Point2D getPositionOnRobot();

	/**
	 * Returns a description of this handler
	 * 
	 * @return a description of this handler
	 */
	public String getDescription();
}
