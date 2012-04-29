package org.cen.robot.device.console;

import java.util.Collection;
import java.util.Properties;

import org.cen.robot.device.console.graph.IConsoleGraph;

public interface IRobotDeviceConsole {
	public void executeCommand(String command, Object parameters);

	public Collection<IRobotDeviceConsoleAction> getActions();

	public IConsoleGraph getGraph();

	public String getName();

	public Properties getProperties();
}
