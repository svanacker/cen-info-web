package org.cen.cup.cup2010.device.specific2010;

public class Specific2010SleepRequest extends Specific2010Request {
	private long duration;

	public Specific2010SleepRequest(long duration) {
		super(Specific2010Device.NAME);
		this.duration = duration;
	}

	public long getDuration() {
		return duration;
	}
}
