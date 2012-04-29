package org.cen.simulRobot.match.event;

import org.cen.ui.gameboard.elements.IMovableElement;


public class TakenElementEvent extends AMatchEvent{

	public TakenElementEvent(String phandler) {
		super(phandler);
		this.handler = phandler;
	}

	public TakenElementEvent(String phandler, IMovableElement takenElement) {
		super(phandler, takenElement);
	}


}
