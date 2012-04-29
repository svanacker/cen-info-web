package org.cen.simulRobot.match.simulMoving;

import java.util.EventListener;

import org.cen.simulRobot.match.simulMoving.event.SimulMovedEvent;

public interface MovingHandlerListener extends EventListener {
	public String getHandlerName();

	public void onMovingHandlerData(SimulMovedEvent event);
}
