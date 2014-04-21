package org.cen.robot.match.strategy.gain.factor.opponent;

import org.cen.robot.match.Opponent;
import org.cen.robot.match.strategy.gain.ITargetGain;

public class NoOpponentGainFactor implements IOpponentGainFactor {

	@Override
	public double getGainFactor(Opponent opponent) {
		// We do not care about
		return ITargetGain.DEFAULT_GAIN;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
