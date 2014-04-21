package org.cen.robot.match;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cen.logging.LoggingUtils;

/**
 * Default implementation of the strategy service based on a events queue that
 * delegate the received match events to the handlers in the order in which they
 * are notified.
 * 
 * @author Emmanuel ZURMELY
 */
public class DefaultMatchStrategy extends AbstractMatchStrategy {
	private final static Logger LOGGER = LoggingUtils.getClassLogger();

	protected Queue<IMatchEvent> queue = new ArrayDeque<IMatchEvent>();

	protected boolean running = false;

	protected void handleEvent(IMatchEvent event) {
		ArrayList<IMatchStrategyHandler> h = new ArrayList<IMatchStrategyHandler>(handlers);
		for (IMatchStrategyHandler handler : h) {
			try {
				handler.handleEvent(event);
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.log(Level.FINE, "the handler " + handler.getName() + " failed to handle the event " + event, e);
			}
		}
	}

	protected void handleEvents() {
		if (running) {
			return;
		}
		running = true;
		while (!queue.isEmpty()) {
			handleEvent(queue.remove());
		}
		running = false;
	}

	public void notifyEvent(IMatchEvent event) {
		queue.add(event);
		handleEvents();
	}
}
