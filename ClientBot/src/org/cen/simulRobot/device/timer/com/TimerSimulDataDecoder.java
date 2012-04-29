package org.cen.simulRobot.device.timer.com;

import java.util.HashSet;
import java.util.Set;

import org.cen.com.IllegalComDataException;
import org.cen.com.InDataDecoder;
import org.cen.com.in.InData;
import org.cen.com.in.UntypedInData;

/**
 * Class which is responsible of decoding data from the configuration which come
 * from the server
 */
public class TimerSimulDataDecoder implements InDataDecoder {
	private static Set<String> handled = new HashSet<String>();

	static {
		handled.add(MatchFinishedSimulInData.HEADER);
	}

	private void checkLength(String data, int l) throws IllegalComDataException {
		if (data.length() != l)
			throw new IllegalComDataException();
	}

	@Override
	public InData decode(String data) throws IllegalComDataException {
		String header = data.substring(0, 1);
		if(header.equals(MatchFinishedSimulInData.HEADER)){
			checkLength(data, 1);
			return new MatchFinishedSimulInData();
		}
		return new UntypedInData(data);
	}

	@Override
	public int getDataLength(String header) {
		if (header.equals(MatchFinishedSimulInData.HEADER)) {
			return 0;
		}
		return 0;
	}

	@Override
	public Set<String> getHandledHeaders() {
		return handled;
	}
}
