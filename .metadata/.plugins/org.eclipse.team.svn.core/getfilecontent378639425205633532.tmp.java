package org.cen.robot.match.strategy.impl;

import java.awt.geom.Point2D;
import java.util.Iterator;

import org.cen.robot.match.strategy.ITarget;
import org.cen.robot.match.strategy.ITargetAction;
import org.cen.robot.match.strategy.ITargetActionList;

public abstract class AbstractTarget implements ITarget {

	private String name;

	private ITargetActionList targetActionList;

	private Point2D position;

	public AbstractTarget(String name, Point2D position) {
		super();
		this.name = name;
		this.position = position;
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

	@Override
	public Point2D getPosition() {
		return position;
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
