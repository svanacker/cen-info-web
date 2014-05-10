package org.cen.ui.web;

import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.IRobotDeviceListener;
import org.cen.robot.device.RobotDeviceResult;
import org.cen.robot.device.battery.BatteryDevice;
import org.cen.robot.device.battery.BatteryReadResult;
import org.cen.robot.services.IRobotService;
import org.cen.robot.services.IRobotServiceProvider;

public class BatteryView implements IRobotService {
	private IRobotServiceProvider servicesProvider;

	private double voltage;

	@Override
	public void setServicesProvider(IRobotServiceProvider provider) {
		servicesProvider = provider;
		IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
		handler.addDeviceListener(new IRobotDeviceListener() {
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
