package org.cen.robot.device.relay;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.event.EventListenerList;

import org.cen.logging.LoggingUtils;

/**
 * Encapsulates the data of an element of the relay.
 */
public class RelayData {

	private final static Logger logger = LoggingUtils.getClassLogger();

	/** Id of the relay (between 1 and 8). */
	private int id;

	/** The value (true = on), (false = off). */
	private boolean value;

	private final EventListenerList listeners = new EventListenerList();

	public RelayData(int id) {
		super();
		this.id = id;
	}

	public void addRelayChangeListener(RelayChangeListener relayChangeListener) {
		listeners.add(RelayChangeListener.class, relayChangeListener);
	}

	/**
	 * Notify all observers that requires to be notify when pid data changes
	 */
	private void fireRelayChangeListener() {
		Object[] l = listeners.getListenerList();
		for (int j = l.length - 2; j >= 0; j -= 2) {
			if (l[j] == RelayChangeListener.class) {
				try {
					((RelayChangeListener) l[j + 1]).onRelayChange(this);
				} catch (Exception ex) {
					logger.log(Level.WARNING, ex.getMessage());
				}
			}
		}
	}

	private void changed() {
		fireRelayChangeListener();
	}

	public int getId() {
		return id;
	}

	public boolean getValue() {
		return value;
	}

	/**
	 * Change the value, and notify the view if it has changed.
	 * 
	 * @param value
	 *            the new value for this relay
	 */
	public void setValue(boolean value) {
		if (value == this.value)
			return;
		this.value = value;
		changed();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[id: " + id + ", value: " + value + "]";
	}

}
