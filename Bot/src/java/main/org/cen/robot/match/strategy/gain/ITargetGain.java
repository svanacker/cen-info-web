package org.cen.robot.match.strategy.gain;

import org.cen.robot.match.Opponent;
import org.cen.robot.match.strategy.ITarget;
import org.cen.robot.match.strategy.ITargetAction;

/**
 * A function interfaces which gain provides a gain depending on several factor
 * such as distance to target, time of match.
 */
public interface ITargetGain {

	/**
	 * Returns the DEFAULT Factor which must be return when a gain factor is not
	 * sensible to a factor.
	 */
	double DEFAULT_GAIN = 1.0f;

	/**
	 * Get the gain. The gain is better when value is higher.
	 * 
	 * @param target
	 * @param distanceToTargetAction
	 * @param matchTime
	 * @return
	 */
	double getGain(ITarget target, ITargetAction targetAction, double distanceToTargetAction, double matchTime, Opponent opponent);
}
