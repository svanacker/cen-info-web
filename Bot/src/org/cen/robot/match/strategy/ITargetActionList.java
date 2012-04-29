package org.cen.robot.match.strategy;


/**
 * Encapsulates a list of action target available to reach the target.
 */
public interface ITargetActionList extends Iterable<ITargetAction> {

	/**
	 * Add a new action.
	 * 
	 * @param targetAction
	 */
	void addTargetAction(ITargetAction targetAction);
}
