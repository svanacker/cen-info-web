package org.cen.simulRobot.device.timer;

import org.cen.com.IComService;
import org.cen.com.RobotInitializedSimulOutData;
import org.cen.com.in.InData;
import org.cen.com.in.InDataListener;
import org.cen.com.out.OutData;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.device.AbstractRobotDevice;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.RobotDeviceRequest;
import org.cen.robot.device.timer.TimerResult;
import org.cen.simulRobot.device.timer.com.MatchFinishedSimulInData;
import org.cen.simulRobot.device.timer.com.MatchStartedSimulOutData;
import org.cen.simulRobot.device.timer.com.TimerSimulDataDecoder;

public class TimerSimulDevice extends AbstractRobotDevice implements InDataListener {
	public final static String NAME = "timer";

	private RobotDeviceRequest request;

	/**
	 * Constructor.
	 */
	public TimerSimulDevice() {
		super(NAME);
	}

	/**
	 * Constructor for a cup specific implementation.
	 * 
	 * @param name
	 *            the name of the device
	 */
	protected TimerSimulDevice(String name) {
		super(name);
	}

	@Override
	public void debug(String debugAction) {
		if (debugAction.equals("initialized")) {
			notifyResult(new RobotInitializingSimulResult(request));
		}else if (debugAction.equals("start")) {
			notifyResult(new MatchStartSimulResult(request));
		}
	}


	@Override
	public void initialize(IRobotServiceProvider servicesProvider) {
		this.servicesProvider = servicesProvider;
		IComService comService = servicesProvider.getService(IComService.class);
		comService.getDecodingService().registerDecoder(new TimerSimulDataDecoder());
		comService.addInDataListener(this);
	}

	@Override
	protected void internalHandleRequest(RobotDeviceRequest request) {
		this.request = request;
		if(request instanceof RobotInitializedSimulRequest) {
			send(new RobotInitializedSimulOutData("XyY"));
		}else if (request instanceof MatchStartedSimulRequest) {
			send(new MatchStartedSimulOutData());
		}
	}

	protected void notifyResult(TimerResult result) {
		IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
		handler.notifyListeners(this, result);
	}

	@Override
	public void onInData(InData data) {
		if(data instanceof MatchFinishedSimulInData){
			notifyResult(new MatchFinishedSimulResult(request));
		}
	}

	void send(OutData outData) {

		IComService comService = servicesProvider.getService(IComService.class);
		comService.writeOutData(outData);
	}
}
