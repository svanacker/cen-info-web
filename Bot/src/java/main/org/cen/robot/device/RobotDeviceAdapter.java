package org.cen.robot.device;

public class RobotDeviceAdapter implements RobotDeviceListener {
	 
	private String name;

	public RobotDeviceAdapter(AbstractRobotDevice device) {
		this(device.getName());
	}

	public RobotDeviceAdapter(String name) {
		super();
		this.name = name;
	}

	public String getDeviceName() {
		return name;
	}

	public void handleResult(RobotDeviceResult result) {
	}
}
