package org.cen.simulRobot.device.navigation.com;

import java.util.HashSet;
import java.util.Set;

import org.cen.com.ComDataUtils;
import org.cen.com.IllegalComDataException;
import org.cen.com.InDataDecoder;
import org.cen.com.in.InData;


public class NavigationSimulDataDecoder implements InDataDecoder {
	final static Set<String> handled = new HashSet<String>();

	static {
		handled.add(NavigationSimulReadInData.HEADER);
		handled.add(StopSimulInData.HEADER);
	}

	private void checkLength(String data, int l) throws IllegalComDataException {
		String header = data.substring(0, 1);
		if(header.equals(NavigationSimulReadInData.HEADER)){
			if (data.length() != 13)
				throw new IllegalComDataException();
		}else if (header.equals(StopSimulInData.HEADER)){
			if (data.length() != 1)
				throw new IllegalComDataException();
		}
	}

	@Override
	public InData decode(String data) throws IllegalComDataException {
		String header = data.substring(0, 1);
		if(header.equals(NavigationSimulReadInData.HEADER)){
			checkLength(data, 13);
			int left = (int) ComDataUtils.parseShortHex(data.substring(1, 5));
			int right = (int) ComDataUtils.parseShortHex(data.substring(5, 9));
			int acceleration = (int) ComDataUtils.parseShortHex(data.substring(9, 11));
			int speed = (int) ComDataUtils.parseShortHex(data.substring(11, 13));
			return new NavigationSimulReadInData(left, right, speed, acceleration);
		}else if(header.equals(StopSimulInData.HEADER)){
			checkLength(data, 1);
			return new StopSimulInData();
		}
		return null;
	}

	@Override
	public int getDataLength(String header) {
		if(header.equals(NavigationSimulReadInData.HEADER)){
			return 12;
		}else if (header.equals(StopSimulInData.HEADER)){
			return 0;
		}else{
			return -1;
		}
	}

	@Override
	public Set<String> getHandledHeaders() {
		return handled;
	}
}
