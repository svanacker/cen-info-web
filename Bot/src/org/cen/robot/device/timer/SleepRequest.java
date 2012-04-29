package org.cen.robot.device.timer;

import org.cen.robot.device.RobotDeviceRequest;

public class SleepRequest extends RobotDeviceRequest {
	private long delay;

	public SleepRequest(long delay) {
		super(TimerDevice.NAME);
		this.delay = delay;
	}

	public long getDelay() {
		return delay;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[delay=" + delay + " ms]";
	}
}
