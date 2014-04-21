package org.cen.robot.match.events;

import org.cen.robot.device.timer.SleepResult;
import org.cen.robot.match.IMatchEvent;

public class TimerEvent implements IMatchEvent {
	private SleepResult result;

	public TimerEvent(SleepResult result) {
		super();
		this.result = result;
	}

	public SleepResult getResult() {
		return result;
	}
}
