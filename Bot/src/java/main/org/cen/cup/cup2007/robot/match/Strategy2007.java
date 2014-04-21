package org.cen.cup.cup2007.robot.match;

import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.RobotPosition;
import org.cen.robot.RobotUtils;
import org.cen.robot.match.IMatchEvent;
import org.cen.robot.match.IMatchStrategyHandler;
import org.cen.robot.match.MatchData;
import org.cen.robot.match.events.MatchConfigurationDone;

public class Strategy2007 implements IMatchStrategyHandler {
	private IRobotServiceProvider servicesProvider;

	public String getName() {
		return getClass().getSimpleName();
	}

	public boolean handleEvent(IMatchEvent event) {
		if (event instanceof MatchConfigurationDone)
			setInitialPosition();
		else
			return false;
		return true;
	}

	private void setInitialPosition() {
		RobotPosition position = RobotUtils.getRobotAttribute(RobotPosition.class, servicesProvider);
		MatchData data = RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);
		switch (data.getSide()) {
		case RED:
			position.set(1950, 2850, -Math.toRadians(135));
			break;
		case VIOLET:
			position.set(1950, 150, Math.toRadians(135));
			break;
		}
	}

	public void setServicesProvider(IRobotServiceProvider provider) {
		servicesProvider = provider;
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}
}
