package org.cen.ui.gameboard;

import java.util.List;

import org.cen.simulRobot.match.simulMoving.ISimulMovingHandler;

public interface ISimulGameBoard extends IGameBoardService{

	public void addElement(IGameBoardElement typeElement);

	public void addElements(List<IGameBoardElement> typeElements);

	public <E extends IGameBoardElement>List<E> getElements(Class<E> elementTypes);

	public ISimulMovingHandler getMovingHandler();

	public void notifyEvent(IGameBoardEvent event);

	public void removeElement(IGameBoardElement typeElement);

	public <E extends IGameBoardElement> void removeElements(List<E> typeElement);
}
