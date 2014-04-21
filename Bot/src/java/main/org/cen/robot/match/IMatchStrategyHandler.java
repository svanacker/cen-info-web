package org.cen.robot.match;

import org.cen.robot.IRobotService;

/**
 * Interface of a strategy handler delegate. The strategy service delegates all
 * received match event to the registered strategy handler.
 * 
 * @author Emmanuel ZURMELY
 */
public interface IMatchStrategyHandler extends IRobotService {

	/**
	 * Returns the name of this strategy handler.
	 * 
	 * @return the name of this strategy handler
	 */
	public String getName();

	/**
	 * Handles the delegated match event and returns true if the events has been
	 * handled. If the handler returns false, the event is delegated to the next
	 * handler registered at the strategy service.
	 * 
	 * @param event
	 *            the event to handle
	 * @return true if the event has been handled, false to delegate it to the
	 *         next handler
	 */
	public boolean handleEvent(IMatchEvent event);

	/**
	 * Starts the handler.
	 */
	public void start();

	/**
	 * Stops the handler.
	 */
	public void stop();
}
