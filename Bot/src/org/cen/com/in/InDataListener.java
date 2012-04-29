package org.cen.com.in;

import java.util.EventListener;

/**
 * The interface for event corresponding to data from the port COM. We do not
 * manage with SerialPortEventListener because it does not support multiple
 * listener, the second problem is to map the data into an object easily.
 * 
 * @author svanacker
 * @version 08/11/2003
 */
public interface InDataListener extends EventListener {

	/**
	 * The interface which is called when data is read from the serial port.
	 * 
	 * @param data
	 *            an object representing the data
	 */
	public void onInData(InData data);
}
