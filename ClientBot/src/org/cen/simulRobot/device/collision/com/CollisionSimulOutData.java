package org.cen.simulRobot.device.collision.com;

import org.cen.com.ComDataUtils;
import org.cen.com.out.OutData;
import org.cen.simulRobot.device.collision.CollisionSimulReadRequest;

public class CollisionSimulOutData extends OutData {

	static final String HEADER = "h";
	static final String SPLIT = "u";
	int angle;
	int distance;

	int left;
	int right;
	public CollisionSimulOutData(CollisionSimulReadRequest request) {
		this.left = request.getLeft();
		this.right = request.getRight();
		this.distance = request.getDistance();
		this.angle = request.getAngle();
	}

	@Override
	public String getArguments() {
		String leftString = ComDataUtils.format(left, 4);
		String rightString = ComDataUtils.format(right, 4);
		String distanceString = ComDataUtils.format(right, 4);
		String angleString = ComDataUtils.format(right, 4);

		return leftString + SPLIT + rightString + SPLIT + distanceString + SPLIT + angleString;
	}

	@Override
	public String getHeader() {
		return HEADER;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[header=" + HEADER + "left=" + left + "right=" + right + "distance=" + distance + "angle=" + angle +"]";
	}
}
