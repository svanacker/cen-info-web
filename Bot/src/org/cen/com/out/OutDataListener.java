package org.cen.com.out;

import java.util.EventListener;

/**
 * Interface to be called when there is a notification of sending
 * outData to the board of the robot
 * @author svanacker
 * @version 10/03/2007
 */
public interface OutDataListener extends EventListener {

	/**
	 * The interface which is called when a outData is sent to the board.
	 * @param outData an object representing the data which is sent to the board.
	 */
	public void onOutDataEvent(OutData outData);
}
