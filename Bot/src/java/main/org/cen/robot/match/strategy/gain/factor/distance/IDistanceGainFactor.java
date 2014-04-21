package org.cen.robot.match.strategy.gain.factor.distance;

/**
 * Interface which decomposes the gain factor for distance.
 */
public interface IDistanceGainFactor {

	/**
	 * Returns the gains factor for that distance and this number of move.
	 * 
	 * @param distance
	 * @param numberOfMove
	 * @return
	 */
	double getGainFactor(double distance, double numberOfMove);
}
