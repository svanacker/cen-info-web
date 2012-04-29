package org.cen.robot.device.relay;

import java.util.EventListener;

/**
 * The interface for event corresponding to a change of settings of a relay.
 * 
 * @author svanacker
 * @version 02/03/2008
 */
public interface RelayChangeListener extends EventListener {
	/**
	 * The interface which is called when parameter of relay change.
	 * 
	 * @param relayData
	 *            an object representing the data which has changed
	 */
	public void onRelayChange(RelayData relayData);
}