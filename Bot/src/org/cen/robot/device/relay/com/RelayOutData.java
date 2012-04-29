package org.cen.robot.device.relay.com;

import org.cen.com.ComDataUtils;
import org.cen.com.out.OutData;
import org.cen.robot.device.relay.RelayData;

/**
 * Encapsulation of a message to change the state of a relay. 
 * @author svanacker
 */
public class RelayOutData extends OutData {
	
	/** The header for the message which is send. */
	private static final String HEADER = "R";

	/** The id of the relay (between 1 and 8). */
	private int id;

	/** The value which must be applied for this relay. */
	private boolean value;

	/**
	 * Build an encapsulation of outData from the object model 
	 * @param servoData the object model which represents servo 
	 */
	public RelayOutData(RelayData relayData) {
		super();
		this.id = relayData.getId();
		this.value = relayData.getValue();
	}

	@Override
	public String getArguments() {
		// First Argument 
		String relayId = ComDataUtils.format(id, 2);
		// Second Argument
		String relayValue = ComDataUtils.format(value);
		return relayId + relayValue;
	}

	@Override
	public String getHeader() {
		return HEADER;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[id: " + id + ", value: " + value + "]";
	}	
}