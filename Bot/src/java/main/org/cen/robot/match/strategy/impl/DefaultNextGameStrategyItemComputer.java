package org.cen.robot.match.strategy.impl;

import java.util.List;

import org.cen.navigation.ITrajectoryService;
import org.cen.navigation.Location;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.RobotUtils;
import org.cen.robot.match.Opponent;
import org.cen.robot.match.strategy.IGameStrategy;
import org.cen.robot.match.strategy.IGameStrategyItem;
import org.cen.robot.match.strategy.IGameStrategyItemList;
import org.cen.robot.match.strategy.INextGameStrategyItemComputer;
import org.cen.robot.match.strategy.ITarget;
import org.cen.robot.match.strategy.ITargetAction;
import org.cen.robot.match.strategy.gain.ITargetGain;
import org.cen.util.Holder;

public class DefaultNextGameStrategyItemComputer implements INextGameStrategyItemComputer {
	protected IRobotServiceProvider servicesProvider;

	protected IGameStrategy gameStrategy;

	public DefaultNextGameStrategyItemComputer(IRobotServiceProvider servicesProvider, IGameStrategy gameStrategy) {
		super();
		this.servicesProvider = servicesProvider;
		this.gameStrategy = gameStrategy;
	}

	@Override
	public ITargetAction getNextTarget(double elapsedMatchTime, Location currentLocation, Holder<List<Location>> path, StringBuilder gainData) {
		Opponent opponent = RobotUtils.getRobotAttribute(Opponent.class, servicesProvider);

		double maxGain = 0.0;
		ITargetAction bestTarget = null;
		path.setValue(null);
		Holder<Integer> cost = new Holder<Integer>(new Integer(0));
		// Parcourt des cibles potentielles
		IGameStrategyItemList items = gameStrategy.getItems();
		for (IGameStrategyItem item : items) {
			ITarget target = item.getTarget();
			if (!target.isAvailable()) {
				continue;
			}
			ITargetGain targetGain = item.getTargetGain();
			for (ITargetAction action : target) {
				List<Location> actionPath = getNavigationPath(currentLocation, action.getStartLocation(), cost);
				double distance = cost.getValue();
				double gain = targetGain.getGain(target, action, distance, elapsedMatchTime, opponent);
				log(gainData, target, gain);
				if (gain > maxGain) {
					maxGain = gain;
					bestTarget = action;
					path.setValue(actionPath);
				}
			}
		}
		if (bestTarget != null) {
			gainData.append("<br>Best target: " + bestTarget.getTarget().toString());
		}
		return bestTarget;
	}

	private void log(StringBuilder b, ITarget target, double gain) {
		if (b != null) {
			b.append("<br>" + target.getName() + " gain: " + gain);
		}
	}

	private List<Location> getNavigationPath(Location currentLocation, Location destination, Holder<Integer> cost) {
		ITrajectoryService trajectoryService = servicesProvider.getService(ITrajectoryService.class);
		List<Location> trajectory = trajectoryService.getPath(currentLocation, destination, cost);
		return trajectory;
	}
}
