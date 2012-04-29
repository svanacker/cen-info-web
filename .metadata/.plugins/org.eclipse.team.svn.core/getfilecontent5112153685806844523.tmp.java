package org.cen.robot.match.strategy.gain.factor.distance;


public class DefaultDistanceGainFactor implements IDistanceGainFactor {

	@Override
	public double getGainFactor(double distance, double numberOfMove) {
		return distance * Math.sqrt(numberOfMove);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

}
