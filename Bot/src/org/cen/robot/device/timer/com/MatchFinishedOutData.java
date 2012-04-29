package org.cen.robot.device.timer.com;

import org.cen.com.out.OutData;

/**
 * Encapsulate the message of the end of the match widh sended to the mainBoard
 * 
 * @author Omar BENOUAMER
 */
public class MatchFinishedOutData extends OutData {
	static final String HEADER = "e";

	@Override
	public String getHeader() {
		return HEADER;
	}
}
