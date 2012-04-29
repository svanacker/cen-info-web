package org.cen.simulRobot.device.navigation.com;

import org.cen.com.ComDataUtils;
import org.cen.com.out.OutData;
import org.cen.robot.device.navigation.position.com.PositionStatus;
import org.cen.simulRobot.device.navigation.AbsolutePositionSimulRequest;

public class AbsolutePositionSimulOutData extends OutData {
	static final String HEADER = "k";

	double alpha;

	PositionStatus status;

	double x;

	double y;

	public AbsolutePositionSimulOutData(AbsolutePositionSimulRequest request) {
		this.x = request.getX();
		this.y = request.getY();
		this.alpha = request.getAlpha() * 1000d;
		this.status = request.getStatus();
	}

	@Override
	public String getArguments() {
		String xString = ComDataUtils.format((int) x, 4);
		String separation = "-";
		String yString = ComDataUtils.format((int) y, 4);
		String alphaString = ComDataUtils.format((int) alpha, 4);
		String statusString = ComDataUtils.format(status.ordinal(), 2);
		return xString + separation + yString + separation + alphaString + separation + statusString;
	}

	@Override
	public String getHeader() {
		return HEADER;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[header=" + HEADER + ", x=" + x + ", y=" + y + ", alpha=" + alpha + ", status=" + status + "]";
	}
}
