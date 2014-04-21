package org.cen.robot.device;

import java.util.EventListener;

/**
 * Listener interface for the robot devices events.
 * 
 * @author Emmanuel ZURMELY
 */
public interface RobotDeviceListener extends EventListener {
	/**
	 * Returns the device name with which this listener is attached to
	 * 
	 * @return the listened device name
	 */
	public String getDeviceName();

	/**
	 * Method invoked when notifying an event.
	 * 
	 * @param result
	 *            the result obtained from the device
	 */
	public void handleResult(RobotDeviceResult result);
}
