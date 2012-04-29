package org.cen.simulRobot.device.gripper.com;

import java.util.HashSet;
import java.util.Set;

import org.cen.com.IllegalComDataException;
import org.cen.com.InDataDecoder;
import org.cen.com.in.InData;

/**
 * Class which is responsible of decoding data from the gripper which come
 * from the server
 * 
 * @author Omar BENOUAMER
 */
public class GripperSimulDataDecoder implements InDataDecoder {
	private static Set<String> handled = new HashSet<String>();

	static {
		handled.add(GripperSimulReadInData.HEADER);
	}

	private void checkLength(String data, int l) throws IllegalComDataException {
		if (data.length() != l)
			throw new IllegalComDataException();
	}

	@Override
	public InData decode(String data) throws IllegalComDataException {
		checkLength(data, 2);
		String message = data.substring(1, 2);
		if (message.equals("t")){
			return new TakeSimulInData();
		}else if (message.equals("d")){
			return new DropSimulInData();
		}
		return null;
	}

	@Override
	public int getDataLength(String header) {
		if (header.equals(GripperSimulReadInData.HEADER)) {
			return 1;
		}
		return 0;
	}

	@Override
	public Set<String> getHandledHeaders() {
		return handled;
	}
}
