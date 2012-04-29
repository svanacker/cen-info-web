package org.cen.simulRobot.device.lcd.com;

import java.util.HashSet;
import java.util.Set;

import org.cen.com.IllegalComDataException;
import org.cen.com.InDataDecoder;
import org.cen.com.in.InData;


public class LCDSimulDataDecoder implements InDataDecoder {
	final static Set<String> handled = new HashSet<String>();

	static {
		handled.add(LCDSimulInData.HEADER);
	}

	private int dataLength;

	@Override
	public InData decode(String data) throws IllegalComDataException {
		String text = "";
		return new LCDSimulInData(text);
	}

	@Override
	public int getDataLength(String header) {
		return dataLength;
	}

	@Override
	public Set<String> getHandledHeaders() {
		return handled;
	}

	public void setDataLength(int adataLength) {
		this.dataLength = (adataLength * 2) + 2;
	}
}
