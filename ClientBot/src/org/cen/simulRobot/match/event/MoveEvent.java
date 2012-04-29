package org.cen.simulRobot.match.event;

import org.cen.navigation.TrajectoryCurve;
import org.cen.ui.gameboard.elements.IMovableElement;

public class MoveEvent extends AMovingHandlerEvent{

	private TrajectoryCurve curve;
	private double speed;

	public MoveEvent(IMovableElement pElement, TrajectoryCurve pCurve, double pSpeed, String phandler) {
		super(phandler, pElement);
		this.movableElement = pElement;
		this.handler = phandler;
		this.speed = pSpeed;
		this.curve = pCurve;
	}

	public MoveEvent(TrajectoryCurve pCurve, double pSpeed, String phandler) {
		super(phandler);
		this.handler = phandler;
		this.speed = pSpeed;
		this.curve = pCurve;
	}

	public TrajectoryCurve getCurve() {
		return curve;
	}


	public double getSpeed() {
		return speed;
	}

}
