package org.cen.cup.cup2008.device.launcher;

import java.util.logging.Logger;

import org.cen.com.IComService;
import org.cen.com.out.OutData;
import org.cen.cup.cup2008.device.launcher.com.LaunchOutData;
import org.cen.logging.LoggingUtils;
import org.cen.robot.device.AbstractRobotDevice;
import org.cen.robot.device.RobotDeviceRequest;
import org.cen.robot.device.servo.com.ServoOutData;

public class LauncherDevice extends AbstractRobotDevice {
	private final static Logger LOGGER = LoggingUtils.getClassLogger();

	public static final String NAME = "launcher";

	private static final int HANDLER_SERVO_INDEX = 2;

	private static final int LIFT_SERVO_INDEX = 1;

	private static final int CONTROLER_SERVO_INDEX = 3;

	public LauncherDevice() {
		super(NAME);
	}

	@Override
	protected void internalHandleRequest(RobotDeviceRequest request) {
		LOGGER.finest("handling request: " + request.toString());
		if (request instanceof LauncherLaunchRequest) {
			send(new LaunchOutData());
		} else if (request instanceof LauncherMoveRequest) {
			ServoOutData data;
			LauncherMoveRequest moveRequest = (LauncherMoveRequest) request;
			switch (moveRequest.getMove()) {
			case CLOSE:
				data = new ServoOutData(HANDLER_SERVO_INDEX, 0, 0x0800);
				break;
			case DOWN:
				data = new ServoOutData(LIFT_SERVO_INDEX, 0, 0x0280);
				break;
			case OPEN:
				data = new ServoOutData(HANDLER_SERVO_INDEX, 0, 0x0400);
				break;
			case UP:
				data = new ServoOutData(LIFT_SERVO_INDEX, 0, 0x0650);
				break;
			default:
				return;
			}
			send(data);
		} else if (request instanceof LauncherLoaderRequest) {
			ServoOutData data;
			LauncherLoaderRequest loaderRequest = (LauncherLoaderRequest) request;
			switch (loaderRequest.getAction()) {
			case LOCK:
				data = new ServoOutData(CONTROLER_SERVO_INDEX, 0, 0x0750);
				break;
			case UNLOCK:
				data = new ServoOutData(CONTROLER_SERVO_INDEX, 0, 0x05A0);
				break;
			default:
				return;
			}
			send(data);
		}
	}

	private void send(OutData data) {
		IComService comService = servicesProvider.getService(IComService.class);
		comService.writeOutData(data);
	}
}
