package org.cen.robot.device.navigation;

import org.cen.robot.device.RobotDeviceResult;

public final class NavigationResult extends RobotDeviceResult {
	public enum NavigationResultStatus {
		COLLISION, EMPTYQUEUE, ENQUEUED, INTERRUPTED, MOVING, REACHED, STOPPED;
	}

	private NavigationResultStatus status;

	public NavigationResult(NavigationRequest request, NavigationResultStatus status) {
		super(request);
		this.status = status;
	}

	public NavigationResultStatus getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return getClass().getName() + "@[status: " + status + "]";
	}
}
