package org.cen.simulRobot.device.timer.com;

import org.cen.com.out.OutData;

/**
 * Serial data for writing the configuration device.
 * 
 * @author Benouamer Omar
 */
public class MatchStartedSimulOutData extends OutData {
	static final String HEADER = "s";


	public MatchStartedSimulOutData(){
		super();
	}

	@Override
	public String getArguments() {
		return null;
	}

	@Override
	public String getHeader() {
		return HEADER;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[header=" + HEADER + "]";
	}
}
