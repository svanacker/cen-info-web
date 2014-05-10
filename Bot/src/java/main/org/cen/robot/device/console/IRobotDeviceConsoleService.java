package org.cen.robot.device.console;

import java.util.List;

import org.cen.robot.services.IRobotService;

public interface IRobotDeviceConsoleService extends IRobotService {
	public List<IRobotDeviceConsole> getConsoles();
}
