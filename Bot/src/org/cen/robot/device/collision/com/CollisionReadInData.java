package org.cen.robot.device.collision.com;

import org.cen.com.in.InData;

@Deprecated
public class CollisionReadInData extends InData {

	// TO CHANGE (deprecated)
	public static final String HEADER = "€";

	private final double angle;

	private final double distance;

	private final long rightPosition;

	private final long leftPosition;

	public CollisionReadInData(long leftPosition, long rightPosition, double distance, double angle) {
		super();
		this.leftPosition = leftPosition;
		this.rightPosition = rightPosition;
		this.distance = distance;
		this.angle = angle;
	}

	public double getAngle() {
		return angle;
	}

	public double getDistance() {
		return distance;
	}

	public long getLeftPosition() {
		return leftPosition;
	}

	public long getRightPosition() {
		return rightPosition;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[angle=" + angle + ", distance=" + distance + ", leftPosition="
				+ leftPosition + ", rightPosition=" + rightPosition + "]";
	}
}
