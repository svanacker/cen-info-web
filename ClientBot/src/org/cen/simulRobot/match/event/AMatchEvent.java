package org.cen.simulRobot.match.event;

import org.cen.robot.match.IMatchEvent;
import org.cen.ui.gameboard.elements.IMovableElement;

public abstract class AMatchEvent implements IMatchEvent{
	protected String handler;
	protected IMovableElement movableElement;

	public AMatchEvent(String phandler) {
		this.handler = phandler;
	}

	public AMatchEvent(String phandler, IMovableElement takenElement) {
		this.movableElement = takenElement;
		this.handler = phandler;
	}

	public String getHandler() {
		return handler;
	}

	public IMovableElement getMovableElement() {
		return movableElement;
	}

	public void setMovableElement(IMovableElement pMovableElement) {
		this.movableElement = pMovableElement;
	}

}
