package org.cen.robot.match.strategy;

/**
 * The lists of the the strategy items.
 * 
 * @author Emmanuel ZURMELY
 */
public interface IGameStrategyItemList extends Iterable<IGameStrategyItem> {

	void addStrategyItem(IGameStrategyItem strategyItem);
}
