package org.cen.robot.match.strategy;

import java.awt.geom.Point2D;

/**
 * Encapsulates a target that a robot can reach to generally obtains poins.
 */
public interface ITarget extends Iterable<ITargetAction> {

	/**
	 * Add a target Action to achieve the target.
	 * 
	 * @param targetAction
	 */
	void addTargetAction(ITargetAction targetAction);

	/**
	 * Returns the actions list.
	 * 
	 * @return
	 */
	ITargetActionList getActionList();

	/**
	 * Returns the gain when reaching a such target.
	 * 
	 * @return
	 */
	double getGain();

	/**
	 * Returns the name of the target.
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Checks whether the target is still available.
	 * 
	 * @return
	 */
	boolean isAvailable();

	/**
	 * Set the availability of the target.
	 * 
	 * @param available
	 */
	void setAvailable(boolean available);

	/**
	 * Returns the position of the target on the gameboard
	 * 
	 * @return
	 */
	Point2D getPosition();
}
