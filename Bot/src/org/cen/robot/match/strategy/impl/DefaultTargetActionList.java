package org.cen.robot.match.strategy.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.cen.robot.match.strategy.ITargetAction;
import org.cen.robot.match.strategy.ITargetActionList;

public class DefaultTargetActionList implements ITargetActionList {

	protected List<ITargetAction> list;

	public DefaultTargetActionList() {
		list = new ArrayList<ITargetAction>();
	}

	@Override
	public Iterator<ITargetAction> iterator() {
		return list.iterator();
	}

	@Override
	public void addTargetAction(ITargetAction targetAction) {
		list.add(targetAction);
	}

}
