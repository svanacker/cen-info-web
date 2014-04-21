package org.cen.cup.cup2008.device.container.com;

import java.util.HashSet;
import java.util.Set;

import org.cen.com.DefaultDecoder;
import org.cen.com.IllegalComDataException;
import org.cen.com.in.InData;

public class ContainerDataDecoder extends DefaultDecoder {

	private static final Set<String> handled = new HashSet<String>();

	static {
		handled.add(ObjectCollectedInData.HEADER);
		handled.add(GetObjectCountInData.HEADER);
	}

	@Override
	public InData decode(String data) throws IllegalComDataException {
		if (data.substring(0, 1).equals(ObjectCollectedInData.HEADER)) {
			return new ObjectCollectedInData(Integer.parseInt(data.substring(1, 3), 16));
		}
		if (data.substring(0, 1).equals(GetObjectCountInData.HEADER)) {
			return new GetObjectCountInData(Integer.parseInt(data.substring(1, 3), 16));
		}
		throw new IllegalComDataException();
	}

	@Override
	public int getDataLength(String header) {
		if (header.equals(GetObjectCountInData.HEADER)) {
			return 2;
		}
		if (header.equals(ObjectCollectedInData.HEADER)) {
			return 2;
		}
		return 0;
	}

	@Override
	public Set<String> getHandledHeaders() {
		return handled;
	}
}
