package org.cen.simulRobot.match.event;

import org.cen.ui.gameboard.elements.IMovableElement;


public class DropedElementEvent extends AMatchEvent{

	public DropedElementEvent(String phandler) {
		super(phandler);
		this.handler = phandler;
	}

	public DropedElementEvent(String phandler, IMovableElement takenElement) {
		super(phandler, takenElement);
	}


}
