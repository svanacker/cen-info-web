package org.cen.simulRobot.device.collision;

import org.cen.com.IComService;
import org.cen.com.in.InData;
import org.cen.com.in.InDataListener;
import org.cen.com.out.OutData;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.device.AbstractRobotDevice;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.RobotDeviceRequest;
import org.cen.simulRobot.device.collision.com.CollisionSimulOutData;
import org.cen.simulRobot.device.navigation.com.NavigationSimulDataDecoder;


public class CollisionSimulDevice extends AbstractRobotDevice implements InDataListener {
	public static final String NAME = "navigation";

	private RobotDeviceRequest request;

	public CollisionSimulDevice() {
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
		this.request = request;
		if (request instanceof CollisionSimulReadRequest) {
			send(new CollisionSimulOutData((CollisionSimulReadRequest)request));
		}

	}

	private void notifyResult(CollisionSimulResult result) {
		IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
		handler.notifyListeners(this, result);
	}

	@Override
	public void onInData(InData data) {

	}

	private void send(OutData outData) {
		IComService comService = servicesProvider.getService(IComService.class);
		comService.writeOutData(outData);
	}
}
