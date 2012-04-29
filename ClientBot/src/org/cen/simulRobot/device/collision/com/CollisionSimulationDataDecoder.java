package org.cen.simulRobot.device.collision.com;

import java.util.HashSet;
import java.util.Set;

import org.cen.com.IllegalComDataException;
import org.cen.com.InDataDecoder;
import org.cen.com.in.InData;


public class CollisionSimulationDataDecoder implements InDataDecoder {
	final static Set<String> handled = new HashSet<String>();

	static {
		handled.add(CollisionSimulReadInData.HEADER);
	}

	@Override
	public InData decode(String data) throws IllegalComDataException {
		//		int left = (int) ComDataUtils.parseShortHex(data.substring(1, 5));
		//		int right = (int) ComDataUtils.parseShortHex(data.substring(5, 9));
		//		int acceleration = (int) ComDataUtils.parseShortHex(data.substring(9, 11));
		//		int speed = (int) ComDataUtils.parseShortHex(data.substring(11, 13));
		//		return new CollisionSimulReadInData(left, right, speed, acceleration);
		return null;
	}

	@Override
	public int getDataLength(String header) {
		if(header.equals("g")){
			return 12;
		}else{
			return -1;
		}
	}

	@Override
	public Set<String> getHandledHeaders() {
		return handled;
	}
}
