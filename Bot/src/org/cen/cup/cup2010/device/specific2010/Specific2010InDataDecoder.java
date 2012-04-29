package org.cen.cup.cup2010.device.specific2010;

import java.util.HashSet;
import java.util.Set;

import org.cen.com.DefaultDecoder;
import org.cen.com.IllegalComDataException;
import org.cen.com.in.InData;
import org.cen.com.in.UntypedInData;
import org.cen.cup.cup2010.device.specific2010.com.CollectDoneInData;
import org.cen.cup.cup2010.device.specific2010.com.CornFixedInData;

public class Specific2010InDataDecoder extends DefaultDecoder {
	static final Set<String> HANDLED = new HashSet<String>();

	static {
		HANDLED.add(CornFixedInData.HEADER);
		HANDLED.add(CollectDoneInData.HEADER);
	}

	@Override
	public InData decode(String data) throws IllegalComDataException {
		String header = data.substring(0, 1);
		if (header.equals(CornFixedInData.HEADER)) {
			return new CornFixedInData();
		} else if (header.equals(CollectDoneInData.HEADER)) {
			return new CollectDoneInData();
		}
		return new UntypedInData(data);
	}

	@Override
	public int getDataLength(String header) {
		return 0;
	}

	@Override
	public Set<String> getHandledHeaders() {
		return HANDLED;
	}
}
