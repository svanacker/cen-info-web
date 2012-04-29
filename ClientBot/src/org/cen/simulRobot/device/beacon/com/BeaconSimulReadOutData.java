package org.cen.simulRobot.device.beacon.com;

import org.cen.com.ComDataUtils;
import org.cen.com.out.OutData;

/**
 * Correspond to the position transmit by the beacon.
 * 
 * @author Omar BENOUAMER
 */
public class BeaconSimulReadOutData extends OutData {
	static final String HEADER = "u";

	private Integer x;
	private Integer y;

	public BeaconSimulReadOutData(int x, int y){
		super();
		this.x = x;
		this.y = y;
	}

	@Override
	public String getArguments() {
		String result = "";
		result += ComDataUtils.format(x, 4);
		result += ComDataUtils.format(y, 4);
		return result;
	}

	@Override
	public String getHeader() {
		return HEADER;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[header=" + HEADER + "x=" + x + "y=" + y + "]";
	}
}
