package org.cen.cup.cup2010.device.specific2010.com;

import org.cen.com.ComDataUtils;
import org.cen.com.out.OutData;
import org.cen.cup.cup2010.device.specific2010.RobotLift2010Request.Action;

public class RobotLift2010OutData extends OutData {
	private int data;

	public RobotLift2010OutData(Action action) {
		super();
		data = action.ordinal();
	}

	@Override
	public String getArguments() {
		return ComDataUtils.format(data, 2);
	}

	@Override
	public String getHeader() {
		return "l";
	}
}
