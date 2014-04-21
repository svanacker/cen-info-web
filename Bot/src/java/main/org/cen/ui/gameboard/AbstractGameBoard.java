package org.cen.ui.gameboard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.event.EventListenerList;

import org.cen.robot.IRobotServiceProvider;

/**
 * Abstract base class of IGameboard implementations.
 */
public abstract class AbstractGameBoard implements IGameBoardService {

	protected EventListenerList listeners = new EventListenerList();

	protected IRobotServiceProvider servicesProvider;

	@Override
	public void addListener(IGameBoardEventListener listener) {
		listeners.add(IGameBoardEventListener.class, listener);
	}

	@Override
	public List<IGameBoardElement> findElements(String elementName) {
		List<IGameBoardElement> found = new ArrayList<IGameBoardElement>();
		List<IGameBoardElement> elements = getElements();
		Iterator<IGameBoardElement> i = elements.iterator();
		while (i.hasNext()) {
			IGameBoardElement e = i.next();
			if (e.getName().equals(elementName)) {
				found.add(e);
			}
		}
		return found;
	}

	/**
	 * Handles a game board event.
	 * 
	 * @param event
	 *            the game board event
	 * @return true if the event has been handled by the receiver, false
	 *         otherwise
	 */
	protected boolean handleEvent(IGameBoardEvent event) {
		// default implementation : the event is not handled
		return false;
	}

	/**
	 * Notifies the event to the listener.
	 * 
	 * @param event
	 *            the event to notify
	 */
	protected void notifyEvent(IGameBoardEvent event) {
		Object[] l = listeners.getListenerList();
		for (int i = l.length - 2; i >= 0; i -= 2) {
			if (l[i] == IGameBoardEventListener.class) {
				((IGameBoardEventListener) l[i + 1]).onGameBoardEvent(event);
			}
		}
	}

	@Override
	public void postEvent(IGameBoardEvent event) {
		if (!handleEvent(event)) {
			notifyEvent(event);
		}
	}

	@Override
	public void removeElements(String elementName) {
		List<IGameBoardElement> elements = getElements();
		List<IGameBoardElement> found = findElements(elementName);
		for (IGameBoardElement e : found) {
			elements.remove(e);
		}
	}

	@Override
	public void removeListener(IGameBoardEventListener listener) {
		listeners.remove(IGameBoardEventListener.class, listener);
	}

	@Override
	public void setServicesProvider(IRobotServiceProvider provider) {
		servicesProvider = provider;
		provider.registerService(IGameBoardService.class, this);
	}
}
