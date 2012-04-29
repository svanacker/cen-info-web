package org.cen.robot.match.strategy.impl;

import java.util.ArrayList;
import java.util.Iterator;

import org.cen.robot.match.strategy.IGameStrategyItem;
import org.cen.robot.match.strategy.IGameStrategyItemList;

public class DefaultGameStrategyItemList implements IGameStrategyItemList {
	private ArrayList<IGameStrategyItem> items = new ArrayList<IGameStrategyItem>();

	@Override
	public void addStrategyItem(IGameStrategyItem strategyItem) {
		items.add(strategyItem);
	}

	@Override
	public Iterator<IGameStrategyItem> iterator() {
		return items.iterator();
	}
}
