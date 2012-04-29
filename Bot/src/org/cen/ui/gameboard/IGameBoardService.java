package org.cen.ui.gameboard;

import java.awt.geom.Rectangle2D;
import java.util.List;

import org.cen.robot.IRobotService;

/**
 * Interface describing the game board.
 */
public interface IGameBoardService extends IRobotService {

	/**
	 * Adds the specified events listener to the listeners list of this game
	 * board object.
	 * 
	 * @param listener
	 *            the listener to add
	 */
	public void addListener(IGameBoardEventListener listener);

	/**
	 * Search for the gameboard elements with the specified name
	 * 
	 * @param elementName
	 *            the name of the elements
	 * @return a list containing the gameboard elements that match the specified
	 *         name
	 */
	public List<IGameBoardElement> findElements(String elementName);

	/**
	 * Returns the ordered list of the elements of the game board.
	 * 
	 * @return a list of game board elements sorted by painting order
	 */
	public List<IGameBoardElement> getElements();

	/**
	 * Returns the playable bounds of the game board.
	 * 
	 * @return the playable bounds of the game board
	 */
	public Rectangle2D getGameplayBounds();

	/**
	 * Returns the visible bounds of the game board.
	 * 
	 * @return the visible bounds of the game board
	 */
	public Rectangle2D getVisibleBounds();

	/**
	 * Posts a game board event to this game board object. The receiver must
	 * process the event or dispatch it to its listeners.
	 * 
	 * @param event
	 *            the event object to post
	 */
	public void postEvent(IGameBoardEvent event);

	/**
	 * Removes all elements with the specified name from the gameboard.
	 * 
	 * @param elementName
	 *            the name of the elements to remove
	 * @since 2010
	 */
	public void removeElements(String elementName);

	/**
	 * Removes the specified events listener from the listeners list of this
	 * game board object.
	 * 
	 * @param listener
	 *            the listener to remove
	 */
	public void removeListener(IGameBoardEventListener listener);
}
