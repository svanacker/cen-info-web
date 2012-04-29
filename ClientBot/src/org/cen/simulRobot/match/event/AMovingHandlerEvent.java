package org.cen.simulRobot.match.event;

import org.cen.ui.gameboard.IGameBoardEvent;
import org.cen.ui.gameboard.elements.IMovableElement;

public class AMovingHandlerEvent extends AMatchEvent implements IGameBoardEvent {

	public AMovingHandlerEvent(String phandler) {
		super(phandler);
	}

	public AMovingHandlerEvent(String phandler, IMovableElement takenElement) {
		super(phandler, takenElement);
	}

}
