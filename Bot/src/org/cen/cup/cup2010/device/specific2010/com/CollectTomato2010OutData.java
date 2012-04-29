package org.cen.cup.cup2010.device.specific2010.com;

import org.cen.com.out.OutData;
import org.cen.cup.cup2010.device.specific2010.CollectTomato2010Request.Action;

public class CollectTomato2010OutData extends OutData {
	private static final String HEADER_ON = "V";

	private static final String HEADER_OFF = "v";

	private Action action;

	public CollectTomato2010OutData(Action action) {
		super();
		this.action = action;
	}

	@Override
	public String getHeader() {
		switch (action) {
		case OFF:
			return HEADER_OFF;
		case ON:
			return HEADER_ON;
		default:
			throw new Error("Unsupported action");
		}
	}
}
