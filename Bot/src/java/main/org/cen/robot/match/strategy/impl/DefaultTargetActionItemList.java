package org.cen.robot.match.strategy.impl;

import java.util.ArrayList;
import java.util.Iterator;

import org.cen.robot.match.strategy.ITargetActionItem;
import org.cen.robot.match.strategy.ITargetActionItemList;

public class DefaultTargetActionItemList implements ITargetActionItemList {
	ArrayList<ITargetActionItem> items = new ArrayList<ITargetActionItem>();

	@Override
	public Iterator<ITargetActionItem> iterator() {
		return items.iterator();
	}

	@Override
	public void addTargetActionItem(ITargetActionItem item) {
		items.add(item);
	}
}
