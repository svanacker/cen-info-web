package org.cen.robot.device.battery;

import org.cen.com.IComService;
import org.cen.com.out.OutData;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.device.AbstractRobotDevice;
import org.cen.robot.device.RobotDeviceRequest;
import org.cen.robot.device.battery.com.BatteryReadOutData;

public class BatteryDevice extends AbstractRobotDevice {
	public static final String NAME = "battery";

	public BatteryDevice(String name) {
		super(NAME);
	}

	@Override
	public void initialize(IRobotServiceProvider servicesProvider) {
		super.initialize(servicesProvider);
		IComService comService = servicesProvider.getService(IComService.class);
		comService.getDecodingService().registerDecoder(new BatteryDataDecoder());
	}

	@Override
	protected void internalHandleRequest(RobotDeviceRequest request) {
		if (request instanceof BatteryReadRequest) {
			send(new BatteryReadOutData());
		}
	}

	private void send(OutData outData) {
		IComService comService = servicesProvider.getService(IComService.class);
		comService.writeOutData(outData);
	}
}
