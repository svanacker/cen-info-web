package org.cen.simulRobot.device.navigation.com;

import org.cen.com.ComDataUtils;
import org.cen.com.out.OutData;
import org.cen.simulRobot.device.navigation.MoveReachedSimulRequest;

@Deprecated
public class MoveReachedSimulOutData extends OutData {

	static final String HEADER = "o";

	int left;

	int right;

	public MoveReachedSimulOutData(MoveReachedSimulRequest request) {
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
