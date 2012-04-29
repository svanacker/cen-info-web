package org.cen.simulRobot.device.timer.com;

public class MatchFinishedSimulInData extends TimerSimulReadInData {
	/** The message header which is sent by the client. */
	static final String HEADER = "e";

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[header=" + HEADER + "]";
	}
}
