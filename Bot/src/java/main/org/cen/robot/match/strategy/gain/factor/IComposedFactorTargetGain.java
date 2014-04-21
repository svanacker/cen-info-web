package org.cen.robot.match.strategy.gain.factor;

import org.cen.robot.match.strategy.gain.ITargetGain;
import org.cen.robot.match.strategy.gain.factor.distance.IDistanceGainFactor;
import org.cen.robot.match.strategy.gain.factor.opponent.IOpponentGainFactor;
import org.cen.robot.match.strategy.gain.factor.time.ITimeGainFactor;

/**
 * A Basic implementationg of target gain which takes into account different sub
 * target gain factor strategy.
 */
public interface IComposedFactorTargetGain extends ITargetGain {

	// time

	ITimeGainFactor getTimeGainFactor();

	void setTimeGainFactor(ITimeGainFactor timeFactor);

	// distance

	IDistanceGainFactor getDistanceGainFactor();

	void setDistanceGainFactor(IDistanceGainFactor distanceFactor);

	// opponent

	IOpponentGainFactor getOpponentGainFactor();

	void setOpponentGainFactor(IOpponentGainFactor gainFactor);

}
