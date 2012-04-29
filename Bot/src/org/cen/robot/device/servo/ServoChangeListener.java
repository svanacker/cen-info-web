package org.cen.robot.device.servo;

import java.util.EventListener;

/**
 * The interface for event corresponding to a change of settings of a servo.
 * 
 * @author svanacker
 * @version 02/03/2008
 */
public interface ServoChangeListener extends EventListener {
	/**
	 * The interface which is called when parameter of servo change.
	 * 
	 * @param servoEngine
	 *            an object representing the data
	 */
	public void onServoChange(ServoData servoData);
}