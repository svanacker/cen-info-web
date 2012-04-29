package org.cen.simulRobot.device.collision;

import org.cen.robot.device.RobotDeviceRequest;
import org.cen.simulRobot.device.navigation.com.NavigationSimulReadInData;

public class CollisionSimulReadResult extends CollisionSimulResult {
	int left, right, speed, acceleration;

	public CollisionSimulReadResult(RobotDeviceRequest arequest, NavigationSimulReadInData aresult) {
		super(arequest);
		this.left = aresult.getLeft();
		this.right = aresult.getRight();
		this.speed = aresult.getSpeed();
		this.acceleration = aresult.getAcceleration();
	}

	public int getAcceleration() {
		return acceleration;
	}

	public int getLeft() {
		return left;
	}

	public int getRight() {
		return right;
	}

	public int getSpeed() {
		return speed;
	}

}
