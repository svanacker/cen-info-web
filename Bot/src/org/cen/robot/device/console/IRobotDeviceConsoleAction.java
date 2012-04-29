package org.cen.robot.device.console;

public interface IRobotDeviceConsoleAction {
	public String getName();

	public String getLabel();

	public void execute(Object parameters);
}
