package org.cen.robot.match;

import java.util.Collection;

import org.cen.robot.IRobotService;

public interface IMatchStrategy extends IRobotService {

	/**
	 * Add a strategy handler to which the match events will be delegated.
	 * 
	 * @param handler
	 *            a handler object that will receive the match events
	 */
	public void addHandler(IMatchStrategyHandler handler);

	/**
	 * Returns all the handler registered for this strategy service.
	 * 
	 * @return a collection of strategy handler that receives the notifications
	 *         of the match events
	 */
	public Collection<IMatchStrategyHandler> getHandlers();

	/**
	 * Notifies the strategy service of the specified match event. This
	 * notification lets the service adapt the current strategy from events
	 * occuring during the match.
	 * 
	 * @param event
	 *            the event to notify
	 */
	public void notifyEvent(IMatchEvent event);

	/**
	 * Removes the specified handler from the registered strategy handlers list.
	 * 
	 * @param handler
	 *            the handler to remove
	 */
	public void removeHandler(IMatchStrategyHandler handler);
}
