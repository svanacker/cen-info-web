package org.cen.cup.cup2010.device.specific2010.com;

import org.cen.com.ComDataUtils;
import org.cen.com.out.OutData;
import org.cen.cup.cup2010.device.specific2010.CollectOrange2010Request.Action;

public class CollectOrange2010OutData extends OutData {
	private static final String HEADER = "a";

	private int data;

	public CollectOrange2010OutData(Action action) {
		super();
		data = action.ordinal();
	}

	@Override
	public String getArguments() {
		return ComDataUtils.format(data, 2);
	}

	@Override
	public String getHeader() {
		return HEADER;
	}
}
