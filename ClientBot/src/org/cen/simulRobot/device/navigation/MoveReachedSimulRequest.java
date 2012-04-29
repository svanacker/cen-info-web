package org.cen.simulRobot.device.navigation;

@Deprecated
public class MoveReachedSimulRequest extends NavigationSimulRequest {
	int left;

	int right;

	public MoveReachedSimulRequest(int pleft, int pright) {
		super(NavigationSimulDevice.NAME);
		this.left = pleft;
		this.right = pright;
	}

	public int getLeft() {
		return left;
	}

	public int getRight() {
		return right;
	}

}