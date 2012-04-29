package org.cen.simulRobot.match.simulMoving;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.event.EventListenerList;

import org.cen.cup.cup2011.simulGameboard.SimulGameBoard2011;
import org.cen.robot.IRobotServiceProvider;
import org.cen.simulRobot.brain.navigation.NavigationSimulHandler;
import org.cen.simulRobot.match.event.AMatchEvent;
import org.cen.simulRobot.match.event.AMovingHandlerEvent;
import org.cen.simulRobot.match.event.MoveEvent;
import org.cen.simulRobot.match.event.StopEvent;
import org.cen.simulRobot.match.simulMoving.event.SimulMovedEvent;
import org.cen.simulRobot.match.simulOpponent.OpponentElemenHandler;
import org.cen.ui.gameboard.IGameBoardService;
import org.cen.ui.gameboard.elements.IMovableElement;
import org.cen.ui.gameboard.elements.SimulOpponentElement;
import org.cen.ui.gameboard.elements.SimulRobotElement;

public abstract class ASimulMovingHandler implements ISimulMovingHandler {

	protected Map<String, SimulMoving> currentMovings;
	private EventListenerList listeners;
	protected int readSpeed;
	protected IRobotServiceProvider servicesProvider;

	public ASimulMovingHandler(IRobotServiceProvider pServicesProvider) {
		super();
		this.servicesProvider = pServicesProvider;
		currentMovings = new HashMap<String, SimulMoving>();
		readSpeed = 1;
		listeners = new EventListenerList();
	}

	@Override
	public void addDeviceListener(MovingHandlerListener listener) {
		listeners.add(MovingHandlerListener.class, listener);
	}

	private void addMove(MoveEvent pEvent) {
		setMovableElement(pEvent);
		SimulMoving currentSimulMoving = new SimulMoving(pEvent, servicesProvider, this);
		String key = currentSimulMoving.getHandler();

		stopMove(pEvent);
		prepareMoving(currentSimulMoving);
		currentMovings.put(key, currentSimulMoving);

	}

	@Override
	public int getReadSpeed() {
		return readSpeed;
	}


	@Override
	public void handleEvent( AMovingHandlerEvent  pEvent){
		if(pEvent instanceof MoveEvent){
			addMove((MoveEvent) pEvent);
		}else if (pEvent instanceof StopEvent){
			stopMove(pEvent);
		}
	}

	@Override
	public void onSimulMoveEvent(SimulMovedEvent pEvent) {
		// Guaranteed to return a non-null array
		Object[] l = listeners.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = l.length - 2; i >= 0; i -= 2) {
			if (l[i] == MovingHandlerListener.class) {
				MovingHandlerListener listener = ((MovingHandlerListener) l[i + 1]);
				if (pEvent.getHandler().equals(listener.getHandlerName())) {
					listener.onMovingHandlerData(pEvent);
				}
			}
		}
	}

	private void prepareMoving(SimulMoving pCurrentSimulMoving) {
		pCurrentSimulMoving.setReadSpeed(readSpeed);
		pCurrentSimulMoving.start();
	}

	@Override
	public void removeDeviceListener(MovingHandlerListener listener) {
		listeners.remove(MovingHandlerListener.class, listener);
	}

	private void setMovableElement(AMatchEvent event) {
		List<IMovableElement> element = new ArrayList<IMovableElement>();
		SimulGameBoard2011 gameBoard = (SimulGameBoard2011)servicesProvider.getService(IGameBoardService.class);
		if( event.getHandler().equals(NavigationSimulHandler.NAME)){
			element.addAll(gameBoard.getElements(SimulRobotElement.class));
		}else if(event.getHandler().equals(OpponentElemenHandler.NAME)){
			element.addAll(gameBoard.getElements(SimulOpponentElement.class));
		}
		event.setMovableElement(element.get(0));
	}

	@Override
	public void setReadSpeed(int readSpeed) {
		this.readSpeed = readSpeed;
	}

	@Override
	public void setServicesProvider(IRobotServiceProvider servicesProvider) {
		this.servicesProvider = servicesProvider;
	}

	@Override
	public void shutdown() {
		if (listeners != null) {
			listeners = null;
		}
		stopAllMoves();
	}

	private void stopAllMoves() {
		Set<Entry<String, SimulMoving>> entrySet = currentMovings.entrySet();
		for (Entry<String, SimulMoving> entry : entrySet) {
			entry.getValue().shutdown();
		}
		currentMovings.clear();
	}

	private void stopMove(AMovingHandlerEvent pEvent) {
		setMovableElement(pEvent);
		String key = pEvent.getHandler();
		if (currentMovings.containsKey(key)){
			currentMovings.get(key).shutdown();
			currentMovings.remove(key);
		}
	}

}