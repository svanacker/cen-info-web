package org.cen.robot.device;

public class RobotDeviceDebugEvent {
	protected RobotDeviceRequest request;

	protected RobotDeviceResult result;

	public RobotDeviceDebugEvent(RobotDeviceRequest request, RobotDeviceResult result) {
		super();
		this.request = request;
		this.result = result;
	}

	public RobotDeviceRequest getRequest() {
		return request;
	}

	public RobotDeviceResult getResult() {
		return result;
	}
}
