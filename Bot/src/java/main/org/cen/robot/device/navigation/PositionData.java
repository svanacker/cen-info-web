package org.cen.robot.device.navigation;

import org.cen.robot.device.navigation.position.com.ReadPositionPulseInData;

/**
 * Object which encapsulates the position of the robot.
 */
public class PositionData {

	private final ReadPositionPulseInData data;

	public PositionData(ReadPositionPulseInData data) {
		super();
		this.data = data;
	}

	public long getLeft() {
		return data.getLeft();
	}

	public long getRight() {
		return data.getRight();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[left=" + getLeft() + ", right=" + getRight() + "]";
	}
}
