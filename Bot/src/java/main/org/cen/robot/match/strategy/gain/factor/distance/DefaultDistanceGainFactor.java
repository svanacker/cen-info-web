package org.cen.robot.match.strategy.gain.factor.distance;

public class DefaultDistanceGainFactor implements IDistanceGainFactor {

	@Override
	public double getGainFactor(double distance, double numberOfMove) {
		if (distance < 0.1 || numberOfMove < 0.1) {
			return 100;
		}
		return 500.0 / (distance * Math.sqrt(numberOfMove));
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

}
