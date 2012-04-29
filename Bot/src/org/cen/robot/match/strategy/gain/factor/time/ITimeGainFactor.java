package org.cen.robot.match.strategy.gain.factor.time;

/**
 * Determines a factor of gain which depends on remaining time.
 */
public interface ITimeGainFactor {

	/**
	 * Returns the gain which is dependant of time.
	 * 
	 * @param currentMatchingTime
	 *            the time between 0 and MatchData.DEFAULT_DURATION
	 * @param timeToAchieveTargetAction
	 * @return
	 */
	double getTimeGainFactor(double currentMatchingTime, double remainingTime, double timeToAchieveTargetAction);
}
