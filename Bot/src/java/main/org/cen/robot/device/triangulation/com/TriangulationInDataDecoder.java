package org.cen.robot.device.triangulation.com;

import java.util.HashSet;
import java.util.Set;

import org.cen.com.IllegalComDataException;
import org.cen.com.decoder.impl.DefaultDecoder;
import org.cen.com.in.InData;

public class TriangulationInDataDecoder extends DefaultDecoder {

	private final static Set<String> handled = new HashSet<String>();

	static {
		handled.add(ReadTriangulationInData.HEADER);
	}

	@Override
	public InData decode(String data) throws IllegalComDataException {
		if (data.length() != 13) {
			throw new IllegalComDataException();
		}
		int c = Integer.parseInt(data.substring(1, 5), 16);
		int l = Integer.parseInt(data.substring(5, 9), 16);
		int p = Integer.parseInt(data.substring(9, 13), 16);
		return new ReadTriangulationInData(c, l, p);
	}

	@Override
	public int getDataLength(String header) {
		if (header.equals(ReadTriangulationInData.HEADER)) {
			return 13;
		}
		return 0;
	}

	@Override
	public Set<String> getHandledHeaders() {
		return handled;
	}
}
