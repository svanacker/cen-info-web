package org.cen.simulRobot.device.collision;


public class CollisionSimulReadRequest extends CollisionSimulRequest {
	int angle;
	int distance;
	int left;
	int right;

	public CollisionSimulReadRequest(int aleft, int aright, int adistance, int aangle) {
		super(CollisionSimulDevice.NAME);
		this.left = aleft;
		this.right = aright;
		this.distance = adistance;
		this.angle = aangle;
	}


	public int getAngle() {
		return angle;
	}

	public int getDistance() {
		return distance;
	}


	public int getLeft() {
		return left;
	}

	public int getRight() {
		return right;
	}
}
