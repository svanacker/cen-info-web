package org.cen.cup.cup2010.device.specific2010.com;

import org.cen.com.ComDataUtils;
import org.cen.com.out.OutData;
import org.cen.cup.cup2010.device.specific2010.CollectCorn2010Request.Action;
import org.cen.cup.cup2010.device.specific2010.CollectCorn2010Request.Side;

public class CollectCorn2010OutData extends OutData {
	private static final String HEADER_DOWN = "P";

	private static final String HEADER_COLLECT = "U";

	private static final String HEADER_SEQUENCE = "J";

	private Action action;

	private int side;

	public CollectCorn2010OutData(Side side, Action action) {
		super();
		this.side = side.ordinal();
		this.action = action;
	}

	@Override
	public String getArguments() {
		return ComDataUtils.format(side, 2);
	}

	@Override
	public String getHeader() {
		switch (action) {
		case DOWN:
			return HEADER_DOWN;
		case COLLECT:
			return HEADER_COLLECT;
		case SEQUENCE:
			return HEADER_SEQUENCE;
		default:
			throw new Error("Unsupported action");
		}
	}
}
