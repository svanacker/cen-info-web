package org.cen.ui.web;

import org.cen.robot.IRobotService;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.RobotDeviceListener;
import org.cen.robot.device.RobotDeviceResult;
import org.cen.robot.device.battery.BatteryDevice;
import org.cen.robot.device.battery.BatteryReadResult;

public class BatteryView implements IRobotService {
	private IRobotServiceProvider servicesProvider;

	private double voltage;

	@Override
	public void setServicesProvider(IRobotServiceProvider provider) {
		servicesProvider = provider;
		IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
		handler.addDeviceListener(new RobotDeviceListener() {
			@Override
			public String getDeviceName() {
				return BatteryDevice.NAME;
			}

			@Override
			public void handleResult(RobotDeviceResult result) {
				if (result instanceof BatteryReadResult) {
					voltage = ((BatteryReadResult) result).getVoltage();
				}
			}
		});
	}

	public double getVoltage() {
		return voltage;
	}

	public int getBatteryState() {
		return 0;
	}

	public void setBatteryState(int batteryState) {
		// TODO
	}

	public void refresh() {

	}
}
