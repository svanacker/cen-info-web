package org.cen.cup.cup2009.device.lift.com;

import org.cen.com.out.OutData;
import org.cen.com.utils.ComDataUtils;

/**
 * OutData to request the lift to go at bottom.
 */
public class LiftGotoBottomOutData extends OutData {
	static final String HEADER = "I";
	
	private static final int DEFAULT_SPEED = 127;	
	
	private int speed;
	
	public LiftGotoBottomOutData(int speed) {
		super();
		this.speed = speed;
	}


	public LiftGotoBottomOutData() {
		this(DEFAULT_SPEED);
	}
	

	@Override
	public String getHeader() {
		return HEADER;
	}


	@Override
	public String getArguments() {
		String speedAsString = ComDataUtils.format(speed, 2);		
		return speedAsString;
	}	
}
