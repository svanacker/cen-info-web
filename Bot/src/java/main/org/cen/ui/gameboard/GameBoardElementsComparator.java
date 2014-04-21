package org.cen.ui.gameboard;

import java.util.Comparator;

/**
 * Comparator used for drawing the elements on the game board.
 * 
 * @author Emmanuel ZURMELY
 */
public class GameBoardElementsComparator implements Comparator<IGameBoardElement> {
	@Override
	public int compare(IGameBoardElement o1, IGameBoardElement o2) {
		return o1.getOrder() - o2.getOrder();
	}
}
