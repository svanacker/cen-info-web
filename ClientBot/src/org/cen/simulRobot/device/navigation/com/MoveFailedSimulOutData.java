package org.cen.simulRobot.device.navigation.com;

import org.cen.com.ComDataUtils;
import org.cen.com.out.OutData;
import org.cen.simulRobot.device.navigation.MoveFailedSimulRequest;

@Deprecated
public class MoveFailedSimulOutData extends OutData {

	static final String HEADER = "f";

	int left;

	int right;

	public MoveFailedSimulOutData(MoveFailedSimulRequest request) {
		this.left = request.getLeft();
		this.right = request.getRight();
	}

	@Override
	public String getArguments() {
		String leftString = ComDataUtils.format(left, 4);
		String separation = "u";
		String rightString = ComDataUtils.format(right, 4);
		return leftString + separation + rightString;
	}

	@Override
	public String getHeader() {
		return HEADER;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[header=" + HEADER + "left=" + left + "right=" + right + "]";
	}
}
