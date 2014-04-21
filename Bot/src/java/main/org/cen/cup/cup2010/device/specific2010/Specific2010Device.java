package org.cen.cup.cup2010.device.specific2010;

import static org.cen.cup.cup2010.Constants2010.DELAY_100MS;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.cen.com.IComService;
import org.cen.com.in.InData;
import org.cen.com.in.InDataListener;
import org.cen.com.out.OutData;
import org.cen.cup.cup2009.device.com.SleepOutData;
import org.cen.cup.cup2010.device.specific2010.CollectCorn2010Request.Side;
import org.cen.cup.cup2010.device.specific2010.ReleaseObjects2010Request.Target;
import org.cen.cup.cup2010.device.specific2010.RobotLift2010Request.Action;
import org.cen.cup.cup2010.device.specific2010.com.CollectCorn2010OutData;
import org.cen.cup.cup2010.device.specific2010.com.CollectDoneInData;
import org.cen.cup.cup2010.device.specific2010.com.CollectOrange2010OutData;
import org.cen.cup.cup2010.device.specific2010.com.CollectTomato2010OutData;
import org.cen.cup.cup2010.device.specific2010.com.CornFixedInData;
import org.cen.cup.cup2010.device.specific2010.com.RobotLift2010OutData;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.device.AbstractRobotDevice;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.RobotDeviceRequest;
import org.cen.robot.device.navigation.com.StopOutData;

public class Specific2010Device extends AbstractRobotDevice implements InDataListener {
	public static final String NAME = "specific2010";

	public Specific2010Device() {
		super(NAME);

	}

	private void addCollectCornOutData(List<OutData> list, Side side, org.cen.cup.cup2010.device.specific2010.CollectCorn2010Request.Action action) {
		addOutData(list, new CollectCorn2010OutData(side, action));
	}

	private void addCollectTomatoOutData(List<OutData> list, org.cen.cup.cup2010.device.specific2010.CollectTomato2010Request.Action action) {
		addOutData(list, getCollectTomatoOutData(action));
	}

	private void addOutData(List<OutData> list, OutData data) {
		if (data != null) {
			list.add(data);
		}
	}

	private void addReleaseObjects2010OutData(List<OutData> list, EnumSet<Target> target) {
		list.add(new CollectCorn2010OutData(null, CollectCorn2010Request.Action.RELEASE));
		sleep(list, DELAY_100MS);
		list.add(new CollectOrange2010OutData(CollectOrange2010Request.Action.RELEASE));
	}

	@Override
	public void debug(String debugAction) {
		IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
		if (debugAction.equals("nextStep")) {
			handler.getRequestDispatcher().nextStep();
		} else if (debugAction.equals("stepByStepOn")) {
			handler.getRequestDispatcher().setStypByStep(true);
		} else if (debugAction.equals("stepByStepOff")) {
			handler.getRequestDispatcher().setStypByStep(false);
		} else if (debugAction.equals("resetPosition")) {
			IComService comService = servicesProvider.getService(IComService.class);
			comService.writeOutData(new StopOutData());
		}
	}

	private OutData getCollectCornOutData(org.cen.cup.cup2010.device.specific2010.CollectCorn2010Request.Side side, org.cen.cup.cup2010.device.specific2010.CollectCorn2010Request.Action action) {
		return new CollectCorn2010OutData(side, action);

		// servo
		// int servoId = -1;
		// vitesse
		// int speed = 0;
		// int value = 0;
		// switch (action) {
		// case CLOSE:
		// switch (side) {
		// case LEFT:
		// servoId = CORN_SERVO_LEFT_OPENCLOSE;
		// break;
		// case RIGHT:
		// servoId = CORN_SERVO_RIGHT_OPENCLOSE;
		// break;
		// }
		// value = CORN_CLOSE_VALUE;
		// speed = CORN_CLOSE_SPEED;
		// break;
		// case DOWN:
		// switch (side) {
		// case LEFT:
		// servoId = CORN_SERVO_LEFT_UPDOWN;
		// break;
		// case RIGHT:
		// servoId = CORN_SERVO_RIGHT_UPDOWN;
		// break;
		// }
		// value = CORN_DOWN_VALUE;
		// speed = CORN_DOWN_SPEED;
		// break;
		// case OPEN:
		// switch (side) {
		// case LEFT:
		// servoId = CORN_SERVO_LEFT_OPENCLOSE;
		// break;
		// case RIGHT:
		// servoId = CORN_SERVO_RIGHT_OPENCLOSE;
		// break;
		// }
		// value = CORN_OPEN_VALUE;
		// speed = CORN_OPEN_SPEED;
		// break;
		// case UP:
		// switch (side) {
		// case LEFT:
		// servoId = CORN_SERVO_LEFT_UPDOWN;
		// break;
		// case RIGHT:
		// servoId = CORN_SERVO_RIGHT_UPDOWN;
		// break;
		// }
		// value = CORN_UP_VALUE;
		// speed = CORN_UP_SPEED;
		// break;
		// case MIDDLE:
		// switch (side) {
		// case LEFT:
		// servoId = CORN_SERVO_LEFT_UPDOWN;
		// break;
		// case RIGHT:
		// servoId = CORN_SERVO_RIGHT_UPDOWN;
		// break;
		// }
		// value = CORN_MIDDLE_VALUE;
		// speed = CORN_MIDDLE_SPEED;
		// break;
		// default:
		// assert false : "invalid value";
		// break;
		// }
		//
		// if (speed > 0 && servoId >= 0) {
		// return new ServoOutData(servoId, speed, value);
		// } else {
		// return null;
		// }
	}

	private OutData getCollectOrangeOutData(org.cen.cup.cup2010.device.specific2010.CollectOrange2010Request.Action action) {
		return new CollectOrange2010OutData(action);
	}

	private OutData getCollectTomatoOutData(org.cen.cup.cup2010.device.specific2010.CollectTomato2010Request.Action action) {
		return new CollectTomato2010OutData(action);

		// int servoId = TOMATO_SERVO;
		// int speed = 0;
		// int value = 0;
		// switch (action) {
		// case ON:
		// speed = TOMATO_ON_SPEED;
		// value = TOMATO_ON_VALUE;
		// break;
		// case OFF:
		// speed = TOMATO_OFF_SPEED;
		// value = TOMATO_OFF_VALUE;
		// break;
		// default:
		// assert false : "invalid value";
		// break;
		// }
		// if (speed > 0 && servoId >= 0) {
		// return new ServoOutData(servoId, speed, value);
		// } else {
		// return null;
		// }
	}

	public List<OutData> getOutData(RobotDeviceRequest request) {
		List<OutData> list = new ArrayList<OutData>();

		if (request instanceof RobotLift2010Request) {
			// Ascenceur
			RobotLift2010Request robotLiftRequest = (RobotLift2010Request) request;
			addOutData(list, getRobotLift2010OutData(robotLiftRequest.getAction()));
		} else if (request instanceof CollectTomato2010Request) {
			// Tomate
			CollectTomato2010Request collectRequest = (CollectTomato2010Request) request;
			addCollectTomatoOutData(list, collectRequest.getAction());
		} else if (request instanceof CollectCorn2010Request) {
			// Maïs
			CollectCorn2010Request collectRequest = (CollectCorn2010Request) request;
			addCollectCornOutData(list, collectRequest.getSide(), collectRequest.getAction());
		} else if (request instanceof CollectOrange2010Request) {
			// Orange
			CollectOrange2010Request collectRequest = (CollectOrange2010Request) request;
			addOutData(list, getCollectOrangeOutData(collectRequest.getAction()));
		} else if (request instanceof ReleaseObjects2010Request) {
			// Lâcher
			ReleaseObjects2010Request releaseRequest = (ReleaseObjects2010Request) request;
			addReleaseObjects2010OutData(list, releaseRequest.getTarget());
		}

		return list;
	}

	private RobotLift2010OutData getRobotLift2010OutData(Action action) {
		return new RobotLift2010OutData(action);
	}

	@Override
	public void initialize(IRobotServiceProvider servicesProvider) {
		super.initialize(servicesProvider);
		IComService comService = servicesProvider.getService(IComService.class);
		comService.getDecodingService().registerDecoder(new Specific2010InDataDecoder());
		comService.addInDataListener(this);
	}

	@Override
	protected void internalHandleRequest(RobotDeviceRequest request) {
		System.out.println("sending " + request);
		List<OutData> data = getOutData(request);
		for (OutData d : data) {
			if (d instanceof SleepOutData) {
				((SleepOutData) d).sleep();
			} else {
				send(d);
			}
		}
	}

	private void notifyListeners(Specific2010Result result) {
		IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
		handler.sendResult(this, result);
	}

	@Override
	public void onInData(InData data) {
		if (data instanceof CornFixedInData) {
			notifyListeners(new CornFixedResult(null));
		} else if (data instanceof CollectDoneInData) {
			notifyListeners(new CollectDoneResult(null));
		}
	}

	private void send(OutData data) {
		IComService comService = servicesProvider.getService(IComService.class);
		comService.writeOutData(data);
	}

	private void sleep(List<OutData> list, int delay) {
		list.add(new SleepOutData(delay));
	}
}
