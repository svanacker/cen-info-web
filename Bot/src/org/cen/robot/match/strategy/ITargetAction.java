package org.cen.robot.match.strategy;

import org.cen.navigation.Location;

/**
 * Describe how the target can be reached.
 */
public interface ITargetAction {

	/**
	 * Returns the location of the target Action.
	 * 
	 * @return
	 */
	Location getEndLocation();

	/**
	 * Returns the list of the action items.
	 * 
	 * @return
	 */
	ITargetActionItemList getItems();

	/**
	 * Returns the first point which is needed to do the target Action.
	 * 
	 * @return
	 */
	Location getStartLocation();

	/**
	 * Returns the target to which this action is related.
	 * 
	 * @return
	 */
	ITarget getTarget();

	/**
	 * Get an estimated time to achieve the actions to reach the target.
	 * 
	 * @return
	 */
	double getTimeToAchieve();
}
