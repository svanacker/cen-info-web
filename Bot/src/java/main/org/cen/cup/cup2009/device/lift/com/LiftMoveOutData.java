package org.cen.cup.cup2009.device.lift.com;

import org.cen.com.out.OutData;
import org.cen.com.utils.ComDataUtils;

public class LiftMoveOutData extends OutData {
	static final String HEADER = "J";

	private static final int DEFAULT_SPEED = 127;

	private int liftIndex;

	private int speed;

	@Override
	public String getHeader() {
		return HEADER;
	}

	public LiftMoveOutData(double factor, int liftIndex) {
		this(factor, liftIndex, DEFAULT_SPEED);
	}

	public LiftMoveOutData(double factor, int liftIndex, int speed) {
		super();
		this.liftIndex = (int) (liftIndex * factor);
		this.speed = speed;
	}

	@Override
	public String getArguments() {
		String liftIndexAsString = ComDataUtils.format(liftIndex, 4);
		String speedAsString = ComDataUtils.format(speed, 2);
		return liftIndexAsString + speedAsString;
	}
}
