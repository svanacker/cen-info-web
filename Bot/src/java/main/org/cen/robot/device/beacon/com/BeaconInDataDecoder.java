package org.cen.robot.device.beacon.com;

import java.util.HashSet;
import java.util.Set;

import org.cen.com.DefaultDecoder;
import org.cen.com.IllegalComDataException;
import org.cen.com.in.InData;

public class BeaconInDataDecoder extends DefaultDecoder {

	private static final Set<String> handled = new HashSet<String>();

	public BeaconInDataDecoder() {
		super();
		handled.add(BeaconInData.HEADER);
	}

	@Override
	public InData decode(String data) throws IllegalComDataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDataLength(String header) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Set<String> getHandledHeaders() {
		return handled;
	}
}
