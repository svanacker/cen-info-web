package org.cen.cup.cup2009.device.com;

import org.cen.com.out.OutData;

public class SleepOutData extends OutData {
	private int delay;

	public SleepOutData(int delay) {
		super();
		this.delay = delay;
	}

	@Override
	public String getHeader() {
		return "";
	}

	public void sleep() {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
