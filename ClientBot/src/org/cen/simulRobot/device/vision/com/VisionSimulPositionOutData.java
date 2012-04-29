package org.cen.simulRobot.device.vision.com;

import org.cen.com.ComDataUtils;

/**
 * Serial data for sending an element absolute position.
 * 
 * @author Omar Benouamer
 */
public class VisionSimulPositionOutData extends VisionSimulReadOutData {


	private Integer x;
	private Integer y;

	public VisionSimulPositionOutData(int x, int y){
		super();
		this.x = x;
		this.y = y;
	}

	@Override
	public String getArguments() {
		String split = "-";
		String result = "";
		result += ComDataUtils.format(x, 4);
		result += split;
		result += ComDataUtils.format(y, 4);
		return result;
	}



	@Override
	public String toString() {
		return getClass().getSimpleName() + "[header=" + HEADER + " x=" + x + " y=" + y + "]";
	}
}
