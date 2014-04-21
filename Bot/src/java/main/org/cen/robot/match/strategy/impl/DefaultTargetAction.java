package org.cen.robot.match.strategy.impl;

import org.cen.navigation.Location;
import org.cen.robot.match.strategy.ITarget;
import org.cen.robot.match.strategy.ITargetAction;
import org.cen.robot.match.strategy.ITargetActionItemList;

/**
 * 
 */
public class DefaultTargetAction implements ITargetAction {
	private double timeToAchieve;

	private Location endLocation;

	private Location startLocation;

	private ITarget target;

	private ITargetActionItemList items = new DefaultTargetActionItemList();

	public DefaultTargetAction(ITarget target, Location startLocation, Location endLocation, double timeToAchieve) {
		super();
		this.timeToAchieve = timeToAchieve;
		this.endLocation = endLocation;
		this.startLocation = startLocation;
		this.target = target;
	}

	@Override
	public Location getEndLocation() {
		return endLocation;
	}

	@Override
	public ITargetActionItemList getItems() {
		return items;
	}

	@Override
	public Location getStartLocation() {
		return startLocation;
	}

	@Override
	public ITarget getTarget() {
		return target;
	}

	@Override
	public double getTimeToAchieve() {
		return timeToAchieve;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{startLocation=" + startLocation + ", timeToAchieve=" + timeToAchieve + " secs}";
	}
}
