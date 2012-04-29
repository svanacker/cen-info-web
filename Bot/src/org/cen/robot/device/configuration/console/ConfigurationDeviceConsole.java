package org.cen.robot.device.configuration.console;

import java.util.Properties;

import org.cen.robot.RobotUtils;
import org.cen.robot.device.AbstractRobotDeviceAction;
import org.cen.robot.device.IRobotDevice;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.RobotDeviceDebugEvent;
import org.cen.robot.device.RobotDeviceDebugListener;
import org.cen.robot.device.configuration.ConfigurationDevice;
import org.cen.robot.device.configuration.ConfigurationReadResult;
import org.cen.robot.device.configuration.ConfigurationResult;
import org.cen.robot.device.console.AbstractRobotDeviceConsole;
import org.cen.robot.match.MatchData;

public class ConfigurationDeviceConsole extends AbstractRobotDeviceConsole implements RobotDeviceDebugListener {
	private static final long serialVersionUID = 1L;

	public ConfigurationDevice device;

	public ConfigurationDeviceConsole(IRobotDevice device) {
		super();
		this.device = (ConfigurationDevice) device;
		IRobotDevicesHandler handler = this.device.getServicesProvider().getService(IRobotDevicesHandler.class);
		handler.addDeviceDebugListener(this);

		addAction(new AbstractRobotDeviceAction("configurationRead", "read") {
			private static final long serialVersionUID = 1L;

			@Override
			public void execute(Object parameters) {
				ConfigurationDeviceConsole.this.device.debug("read");
			}
		});
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public String getName() {
		return device.getName();
	}

	@Override
	public void debugEvent(RobotDeviceDebugEvent e) {
		ConfigurationResult result = (ConfigurationResult) e.getResult();

		if (properties == null) {
			properties = new Properties();
		}

		MatchData data = RobotUtils.getRobotAttribute(MatchData.class, this.device.getServicesProvider());
		properties.putAll(data.getProperties());

		if (result instanceof ConfigurationReadResult) {
			properties.put("rawValue", result.toString());
		}
	}

	@Override
	public String getDeviceName() {
		return device.getName();
	}
}
