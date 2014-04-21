package org.cen.cup.cup2011.ui.web;

import org.cen.cup.cup2011.device.gripper2011.GripperDownRequest2011;
import org.cen.cup.cup2011.device.gripper2011.GripperOpenNoDelayRequest2011;
import org.cen.cup.cup2011.device.gripper2011.GripperUpRequest2011;
import org.cen.cup.cup2011.device.gripper2011.PawnDropRequest2011;
import org.cen.cup.cup2011.device.gripper2011.PawnPickUpRequest2011;
import org.cen.robot.IRobotService;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.device.DeviceRequestDispatcher;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.RobotDeviceRequest;
import org.cen.robot.device.navigation.MoveRequest;
import org.cen.robot.device.navigation.RotationOneWheelRequest;
import org.cen.robot.device.navigation.RotationRequest;

/**
 * Backing bean for that actions view.
 */
public class Actions2011View implements IRobotService {
	private IRobotServiceProvider servicesProvider;

	public void backward() {
		sendRequest(new MoveRequest(-200));
	}

	public void closePlier() {
		sendRequest(new PawnPickUpRequest2011());
	}

	public void forward() {
		sendRequest(new MoveRequest(200));
	}

	private DeviceRequestDispatcher getDispatcher() {
		IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
		return handler.getRequestDispatcher();
	}

	public void left() {
		sendRequest(new RotationRequest(Math.PI / 2));
	}

	public void leftStart() {
		sendRequest(new RotationOneWheelRequest(Math.toRadians(22.5)));
	}

	public void liftDown() {
		sendRequest(new GripperDownRequest2011());
	}

	public void liftToBottom() {
		sendRequest(new GripperOpenNoDelayRequest2011());
	}

	public void liftUp() {
		sendRequest(new GripperUpRequest2011());
	}

	public void openPlier() {
		sendRequest(new PawnDropRequest2011());
	}

	public void right() {
		sendRequest(new RotationRequest(-Math.PI / 2));
	}

	public void rightStart() {
		sendRequest(new RotationOneWheelRequest(Math.toRadians(-22.5)));
	}

	private void sendRequest(RobotDeviceRequest request) {
		getDispatcher().sendRequest(request);
	}

	@Override
	public void setServicesProvider(IRobotServiceProvider provider) {
		servicesProvider = provider;
	}
}
