package org.cen.com;

import org.cen.com.out.OutData;
import org.cen.robot.IRobotService;

/**
 * Interface who defined the contracts to manage in and out data from and to a
 * client.
 */
public interface IComService extends IRobotService, IComServiceListener {

	/**
	 * The acknowlegment character which is send by the micro-controller when
	 * receiving instruction from the PC.
	 */
	char MICRO_CONTROLLER_ACK = 'a';

	/**
	 * Returns the service used for decoding incoming data.
	 * 
	 * @return the service object use to decode incoming data
	 */
	InDataDecodingService getDecodingService();

	/**
	 * Shuts the service down and then reinitializes the service.
	 */
	void reconnect();

	/**
	 * Sends data object to the client (microcontroller).
	 * 
	 * @param outData
	 *            the data to send
	 */
	void writeOutData(OutData outData);

	/**
	 * Simulate input from the client (microcontroller).
	 * 
	 * @param inData
	 *            the data we receive
	 */
	void writeInData(String inData);
}