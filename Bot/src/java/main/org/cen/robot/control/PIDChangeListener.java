package org.cen.robot.control;

import java.util.EventListener;

/**
 * The interface for event corresponding to a change of settings of the PID.
 */
public interface PIDChangeListener extends EventListener {

	/**
	 * The interface which is called when parameter of pid change.
	 * 
	 * @param pidEngine
	 *            an object representing the data
	 */
	public void onPIDChange(PIDData pidEngine);
}