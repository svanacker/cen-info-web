package org.cen.robot.match.strategy;

import org.cen.robot.match.strategy.gain.ITargetGain;

/**
 * An element of a strategy that defines a target and handles the gain.
 * 
 * @author Emmanuel ZURMELY
 */
public interface IGameStrategyItem {

	ITarget getTarget();

	ITargetGain getTargetGain();
}
