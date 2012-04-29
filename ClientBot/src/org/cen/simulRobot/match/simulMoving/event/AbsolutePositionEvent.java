package org.cen.simulRobot.match.simulMoving.event;

import org.cen.robot.device.navigation.position.com.PositionStatus;

public class AbsolutePositionEvent extends SimulMovedEvent {

	public AbsolutePositionEvent(double pX, double pY, double pAlpha,
			PositionStatus pStatus, String pHandler) {
		super(pX, pY, pAlpha, pStatus, pHandler);
	}
}
