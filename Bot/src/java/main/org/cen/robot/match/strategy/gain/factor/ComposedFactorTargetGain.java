package org.cen.robot.match.strategy.gain.factor;

import org.cen.robot.match.Opponent;
import org.cen.robot.match.strategy.ITarget;
import org.cen.robot.match.strategy.ITargetAction;
import org.cen.robot.match.strategy.gain.factor.distance.DefaultDistanceGainFactor;
import org.cen.robot.match.strategy.gain.factor.distance.IDistanceGainFactor;
import org.cen.robot.match.strategy.gain.factor.opponent.IOpponentGainFactor;
import org.cen.robot.match.strategy.gain.factor.opponent.NoOpponentGainFactor;
import org.cen.robot.match.strategy.gain.factor.time.ConstantTimeGainFactor;
import org.cen.robot.match.strategy.gain.factor.time.ITimeGainFactor;

/**
 * Implementation which decomposes the gain into several gain factor, to be able
 * to reuse it and match them to produce IA.
 */
public class ComposedFactorTargetGain implements IComposedFactorTargetGain {

	private ITimeGainFactor timeFactor = new ConstantTimeGainFactor();

	private IDistanceGainFactor distanceFactor = new DefaultDistanceGainFactor();

	private IOpponentGainFactor opponentGainFactor = new NoOpponentGainFactor();

	@Override
	public double getGain(ITarget target, ITargetAction targetAction, double distanceToTargetAction, double currentMatchingTime, Opponent opponent) {
		double result;

		// Basic Gain
		double targetGainFactorValue = target.getGain();

		// Distance : TODO
		double distanceGainFactorValue = distanceToTargetAction;
		if (distanceFactor != null) {
			distanceGainFactorValue = distanceFactor.getGainFactor(distanceToTargetAction, 1.0);
		}

		// Time : TODO : Constant to remove
		double timeToAchieve = targetAction.getTimeToAchieve();
		double timeFactorValue = timeToAchieve;
		if (timeFactor != null) {
			timeFactorValue = timeFactor.getTimeGainFactor(currentMatchingTime, 90000, timeToAchieve);
		}

		double obstacleGainFactorValue = 1.0;
		if (opponentGainFactor != null) {
			obstacleGainFactorValue = opponentGainFactor.getGainFactor(opponent);
		}

		result = targetGainFactorValue * distanceGainFactorValue * obstacleGainFactorValue * timeFactorValue;

		return result;
	}

	@Override
	public ITimeGainFactor getTimeGainFactor() {
		return timeFactor;
	}

	@Override
	public void setTimeGainFactor(ITimeGainFactor timeFactor) {
		this.timeFactor = timeFactor;
	}

	@Override
	public IDistanceGainFactor getDistanceGainFactor() {
		return distanceFactor;
	}

	@Override
	public void setDistanceGainFactor(IDistanceGainFactor distanceFactor) {
		this.distanceFactor = distanceFactor;
	}

	@Override
	public IOpponentGainFactor getOpponentGainFactor() {
		return opponentGainFactor;
	}

	@Override
	public void setOpponentGainFactor(IOpponentGainFactor gainFactor) {
		this.opponentGainFactor = gainFactor;
	}

}
