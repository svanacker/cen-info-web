package org.cen.robot.device.beacon;

import org.cen.robot.device.AbstractRobotDevice;
import org.cen.robot.device.RobotDeviceRequest;

public class BeaconCommunicationDevice extends AbstractRobotDevice {
	private static final String NAME = "beaconCommunication";

	public BeaconCommunicationDevice() {
		super(NAME);
	}

	@Override
	protected void internalHandleRequest(RobotDeviceRequest request) {
	}
}
