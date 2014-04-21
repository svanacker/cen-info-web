package org.cen.ui.gameboard;

import java.util.EventListener;

/**
 * Interface of game board events listeners.
 * 
 * @author Emmanuel ZURMELY
 */
public interface IGameBoardEventListener extends EventListener {
	/**
	 * Notification method for a game board related event.
	 * 
	 * @param event
	 *            the notified event
	 */
	public void onGameBoardEvent(IGameBoardEvent event);
}
