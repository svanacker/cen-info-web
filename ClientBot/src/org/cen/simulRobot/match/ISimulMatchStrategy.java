package org.cen.simulRobot.match;

import org.cen.robot.IRobotService;
import org.cen.robot.match.IMatchEvent;
import org.cen.simulRobot.brain.navigation.NavigationSimulHandler;

public interface ISimulMatchStrategy extends IRobotService {

	public NavigationSimulHandler getNavigationHandler();

	public void notifyEvent(IMatchEvent event);

	public void shutdown();

}
