package org.cen.robot.device.console;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.cen.robot.device.console.graph.GrappaStateMachineGraph;
import org.cen.robot.device.console.graph.IConsoleGraph;

public abstract class AbstractRobotDeviceConsole implements IRobotDeviceConsole {
	protected Map<String, IRobotDeviceConsoleAction> actions = new HashMap<String, IRobotDeviceConsoleAction>();

	protected IConsoleGraph graph;

	protected Properties properties;

	protected void addAction(IRobotDeviceConsoleAction action) {
		actions.put(action.getName(), action);
	}

	@Override
	public void executeCommand(String command, Object parameters) {
		IRobotDeviceConsoleAction action = actions.get(command);
		if (action != null) {
			action.execute(parameters);
		}
	}

	@Override
	public Collection<IRobotDeviceConsoleAction> getActions() {
		return actions.values();
	}

	@Override
	public IConsoleGraph getGraph() {
		if (graph == null) {
			String graphName = getGraphName();
			if (graphName == null) {
				return null;
			}

			URL resource = getClass().getClassLoader().getResource(graphName);
			graph = new GrappaStateMachineGraph(resource);
		}
		return graph;
	}

	protected String getGraphName() {
		return null;
	}

	public Properties getProperties() {
		return properties;
	}
}
