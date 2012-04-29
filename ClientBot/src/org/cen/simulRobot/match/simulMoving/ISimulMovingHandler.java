package org.cen.simulRobot.match.simulMoving;

import org.cen.robot.IRobotServiceProvider;
import org.cen.simulRobot.match.event.AMovingHandlerEvent;
import org.cen.simulRobot.match.simulMoving.event.SimulMovedEvent;

public interface ISimulMovingHandler {


	public abstract void addDeviceListener(MovingHandlerListener listener);

	public abstract int getReadSpeed();

	public abstract void handleEvent( AMovingHandlerEvent  pEvent);

	public abstract void onSimulMoveEvent(SimulMovedEvent pEvent);

	public abstract void removeDeviceListener(MovingHandlerListener listener);

	public abstract void setReadSpeed(int readSpeed);

	public abstract void setServicesProvider(
			IRobotServiceProvider servicesProvider);

	public abstract void shutdown();


}