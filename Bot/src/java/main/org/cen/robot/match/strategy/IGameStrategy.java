package org.cen.robot.match.strategy;

/**
 * A strategy define which targets will be reached, and the priority of this
 * targets.
 */
public interface IGameStrategy {

	/**
	 * The name of the strategy.
	 * 
	 * @return
	 */
	String getName();

	/**
	 * The strategy items.
	 * 
	 * @return the strategy items
	 */
	IGameStrategyItemList getItems();
}
