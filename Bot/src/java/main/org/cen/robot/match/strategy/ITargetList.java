package org.cen.robot.match.strategy;

public interface ITargetList extends Iterable<ITarget> {

	void registerTarget(ITarget target);

	ITarget getTarget(String name);
}
