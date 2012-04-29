package org.cen.robot.match.strategy.gain.factor.time;

import org.cen.robot.match.strategy.gain.ITargetGain;

/**
 * Strategy which do not depends on time to achieve it except when there are not
 * time any more.
 */
public class ConstantTimeGainFactor implements ITimeGainFactor {

	@Override
	public double getTimeGainFactor(double currentMatchingTime, double remainingTime, double timeToAchieveTargetAction) {
		if (remainingTime == 0.0) {
			return 0.0;
		}
		if (timeToAchieveTargetAction < remainingTime) {
			return ITargetGain.DEFAULT_GAIN;
		}
		// we can try it, but we discourage it
		return Math.pow(remainingTime / timeToAchieveTargetAction, 3.0f);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
