package org.cen.robot.device;

import java.util.Map;

import org.cen.robot.IRobotServiceProvider;

public interface IRobotDevice {
	public String getName();

	public Map<String, ?> getProperties();

	public Object getProperty(String name);

	public void handleRequest(RobotDeviceRequest request);

	public void initialize(IRobotServiceProvider servicesProvider);

	public boolean isEnabled();

	public void setEnabled(boolean enabled);

	public void setProperty(String propertyName, Object value);
}
