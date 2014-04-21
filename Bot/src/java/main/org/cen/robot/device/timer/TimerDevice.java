package org.cen.robot.device.timer;

import org.cen.com.IComService;
import org.cen.com.in.InData;
import org.cen.com.in.InDataListener;
import org.cen.com.out.OutData;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.device.AbstractRobotDevice;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.RobotDeviceRequest;
import org.cen.robot.device.timer.com.MatchFinishedInData;
import org.cen.robot.device.timer.com.MatchFinishedOutData;
import org.cen.robot.device.timer.com.MatchStartedInData;
import org.cen.robot.device.timer.com.RobotInitializedInData;
import org.cen.robot.device.timer.com.TimerDataDecoder;

/**
 * Object representing the timer device. The timer is used to trigger
 * notifications after a specified amount of time.
 * 
 * @author Emmanuel ZURMELY
 */
public final class TimerDevice extends AbstractRobotDevice implements InDataListener {
	public static final String NAME = "timer";

	/**
	 * Constructor.
	 */
	public TimerDevice() {
		super(NAME);
	}

	@Override
	public void debug(String debugAction) {
		if (debugAction.equals("start")) {
			onInData(new MatchStartedInData());
		} else if (debugAction.equals("initialized")) {
			onInData(new RobotInitializedInData());
		}
	}

	private OutData getOutData(RobotDeviceRequest request) {
		if (request instanceof MatchFinishedRequest) {
			return new MatchFinishedOutData();
		}
		return null;
	}

	@Override
	public void initialize(IRobotServiceProvider servicesProvider) {
		super.initialize(servicesProvider);
		IComService comService = servicesProvider.getService(IComService.class);
		comService.getDecodingService().registerDecoder(new TimerDataDecoder());
		comService.addInDataListener(this);
	}

	@Override
	protected void internalHandleRequest(RobotDeviceRequest request) {
		if (request instanceof SleepRequest) {
			SleepRequest r = (SleepRequest) request;
			try {
				Thread.sleep(r.getDelay());
			} catch (Exception e) {
				e.printStackTrace();
			}
			notifyResult(new SleepResult(r));
		} else if (request instanceof MatchFinishedRequest) {
			sendData(request);
		}
	}

	private void notifyResult(TimerResult result) {
		IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
		handler.sendResult(this, result);
	}

	@Override
	public void onInData(InData data) {
		if (data instanceof MatchStartedInData) {
			notifyResult(new MatchStartedResult(null));
		} else if (data instanceof RobotInitializedInData) {
			notifyResult(new RobotInitializedResult(null));
		} else if (data instanceof MatchFinishedInData) {
			notifyResult(new MatchFinishedResult(null));
		}
	}

	void sendData(RobotDeviceRequest request) {
		IComService comService = servicesProvider.getService(IComService.class);
		OutData data = getOutData(request);
		comService.writeOutData(data);
	}
}
