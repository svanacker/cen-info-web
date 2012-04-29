package org.cen.simulRobot.device.navigation;

import org.cen.com.IComService;
import org.cen.com.in.InData;
import org.cen.com.in.InDataListener;
import org.cen.com.out.OutData;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.device.AbstractRobotDevice;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.RobotDeviceRequest;
import org.cen.simulRobot.device.com.AckSimulOutData;
import org.cen.simulRobot.device.navigation.com.AbsolutePositionSimulOutData;
import org.cen.simulRobot.device.navigation.com.MoveFailedSimulOutData;
import org.cen.simulRobot.device.navigation.com.MoveReachedSimulOutData;
import org.cen.simulRobot.device.navigation.com.NavigationSimulDataDecoder;
import org.cen.simulRobot.device.navigation.com.NavigationSimulReadInData;
import org.cen.simulRobot.device.navigation.com.RelativePositionSimulOutData;
import org.cen.simulRobot.device.navigation.com.StopSimulInData;

public class NavigationSimulDevice extends AbstractRobotDevice implements InDataListener {
	public static final String NAME = "navigation";

	public NavigationSimulDevice() {
		super(NAME);
	}

	@Override
	public void initialize(IRobotServiceProvider servicesProvider) {
		this.servicesProvider = servicesProvider;
		IComService comService = servicesProvider.getService(IComService.class);
		comService.getDecodingService().registerDecoder(new NavigationSimulDataDecoder());
		comService.addInDataListener(this);
	}

	@Override
	protected void internalHandleRequest(RobotDeviceRequest request) {

		if (request instanceof RelativePositionSimulRequest) {
			send(new RelativePositionSimulOutData((RelativePositionSimulRequest) request));
		} else if (request instanceof AbsolutePositionSimulRequest) {
			send(new AbsolutePositionSimulOutData((AbsolutePositionSimulRequest) request));
		}
		else if (request instanceof MoveFailedSimulRequest) {
			send(new MoveFailedSimulOutData((MoveFailedSimulRequest) request));
		} else if (request instanceof MoveReachedSimulRequest) {
			send(new MoveReachedSimulOutData((MoveReachedSimulRequest) request));
		}
	}

	private void notifyResult(NavigationSimulResult result) {
		IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
		handler.notifyListeners(this, result);
	}

	@Override
	public void onInData(InData data) {
		if (data instanceof NavigationSimulReadInData) {
			IComService comService = servicesProvider.getService(IComService.class);
			comService.writeOutData(new AckSimulOutData("g"));

			NavigationSimulReadInData d = (NavigationSimulReadInData) data;
			// Notify the move
			notifyResult(new NavigationSimulReadResult(null, d));
		}else if (data instanceof StopSimulInData) {
			IComService comService = servicesProvider.getService(IComService.class);
			comService.writeOutData(new AckSimulOutData("z"));

			StopSimulInData d = (StopSimulInData) data;
			// Notify the move
			notifyResult(new StopSimulResult(null, d));
		}
	}

	private void send(OutData outData) {
		IComService comService = servicesProvider.getService(IComService.class);
		comService.writeOutData(outData);
	}
}
