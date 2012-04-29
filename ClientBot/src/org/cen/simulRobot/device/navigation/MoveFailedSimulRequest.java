package org.cen.simulRobot.device.navigation;

@Deprecated
public class MoveFailedSimulRequest extends NavigationSimulRequest {
	int left;

	int right;

	public MoveFailedSimulRequest(int aleft, int aright) {
		super(NavigationSimulDevice.NAME);
		this.left = aleft;
		this.right = aright;
	}

	public int getLeft() {
		return left;
	}

	public int getRight() {
		return right;
	}
}
