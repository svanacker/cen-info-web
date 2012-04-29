package org.cen.simulRobot.match.simulMoving.event;

import org.cen.robot.device.navigation.position.com.PositionStatus;
import org.cen.robot.match.IMatchEvent;

public abstract class SimulMovedEvent implements IMatchEvent{

	private double alpha;
	private String handler;
	private PositionStatus status;
	private double x;
	private double y;


	public SimulMovedEvent(double pX, double pY, double pAlpha,
			PositionStatus pStatus, String pHandler) {
		this.x = pX;
		this.y = pY;
		this.alpha = pAlpha;
		this.status = pStatus;
		this.handler = pHandler;
	}

	public double getAlpha() {
		return alpha;
	}

	public String getHandler() {
		return handler;
	}

	public PositionStatus getStatus() {
		return status;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}


}
