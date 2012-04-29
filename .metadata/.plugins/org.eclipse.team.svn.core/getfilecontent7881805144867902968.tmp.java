package org.cen.robot.match.strategy.impl;

import java.util.Iterator;

import org.cen.robot.match.strategy.ITarget;
import org.cen.robot.match.strategy.ITargetAction;
import org.cen.robot.match.strategy.ITargetActionList;

public abstract class AbstractTarget implements ITarget {

	private String name;

	private ITargetActionList targetActionList;

	public AbstractTarget(String name) {
		super();
		this.name = name;
		targetActionList = new DefaultTargetActionList();
	}

	@Override
	public void addTargetAction(ITargetAction targetAction) {
		targetActionList.addTargetAction(targetAction);
	}

	@Override
	public ITargetActionList getActionList() {
		return targetActionList;
	}

	@Override
	public String getName() {
		return name;
	}

	public ITargetActionList getTargetActionList() {
		return targetActionList;
	}

	@Override
	public Iterator<ITargetAction> iterator() {
		return targetActionList.iterator();
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{name=" + name + "}";
	}

}
