package org.cen.robot.match.strategy.impl;

import org.cen.robot.match.strategy.IGameStrategyItem;
import org.cen.robot.match.strategy.ITarget;
import org.cen.robot.match.strategy.gain.ITargetGain;

public class DefaultGameStrategyItem implements IGameStrategyItem {
	protected ITargetGain targetGain;

	public DefaultGameStrategyItem(ITarget target, ITargetGain targetGain) {
		super();
		this.targetGain = targetGain;
		this.target = target;
	}

	protected ITarget target;

	@Override
	public ITarget getTarget() {
		return target;
	}

	@Override
	public ITargetGain getTargetGain() {
		return targetGain;
	}
}
