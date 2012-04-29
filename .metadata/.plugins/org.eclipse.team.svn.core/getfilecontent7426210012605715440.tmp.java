package org.cen.robot.match.strategy.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.cen.robot.match.strategy.IGameStrategy;

public class DefaultGameStrategyList implements Iterable<IGameStrategy> {
	protected List<IGameStrategy> list;

	public DefaultGameStrategyList() {
		list = new ArrayList<IGameStrategy>();
	}

	@Override
	public Iterator<IGameStrategy> iterator() {
		return list.iterator();
	}

	public void addStrategy(IGameStrategy strategy) {
		list.add(strategy);
	}
}
