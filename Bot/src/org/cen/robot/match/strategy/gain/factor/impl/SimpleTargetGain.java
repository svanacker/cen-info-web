package org.cen.robot.match.strategy.gain.factor.impl;

import org.cen.robot.match.strategy.gain.factor.ComposedFactorTargetGain;
import org.cen.robot.match.strategy.gain.factor.distance.DefaultDistanceGainFactor;
import org.cen.robot.match.strategy.gain.factor.opponent.NoOpponentGainFactor;
import org.cen.robot.match.strategy.gain.factor.time.ConstantTimeGainFactor;

/**
 * A very simple implementation which depends on target gain and distance.
 */
public class SimpleTargetGain extends ComposedFactorTargetGain {

	public SimpleTargetGain() {
		setDistanceGainFactor(new DefaultDistanceGainFactor());
		setTimeGainFactor(new ConstantTimeGainFactor());
		setOpponentGainFactor(new NoOpponentGainFactor());
	}
}
