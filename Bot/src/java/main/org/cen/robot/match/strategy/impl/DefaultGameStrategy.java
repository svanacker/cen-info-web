package org.cen.robot.match.strategy.impl;

import org.cen.robot.match.strategy.IGameStrategy;
import org.cen.robot.match.strategy.IGameStrategyItemList;

public class DefaultGameStrategy implements IGameStrategy {

	private String name;

	private IGameStrategyItemList items;

	public DefaultGameStrategy(String name) {
		super();
		this.name = name;
		items = new DefaultGameStrategyItemList();
	}

	@Override
	public IGameStrategyItemList getItems() {
		return items;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{name=" + name + "}";
	}
}
