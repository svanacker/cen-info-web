package org.cen.robot.device.console.graph;

import java.net.URL;

import att.grappa.Graph;
import att.grappa.Node;

public class GrappaStateMachineGraph extends GrappaConsoleGraph implements IStateMachineGraph {
	public GrappaStateMachineGraph() {
		super();
	}

	public GrappaStateMachineGraph(URL resource) {
		super(resource);
	}

	private String activeState;

	@Override
	public void setActiveState(String name) {
		Graph graph = getGraph();
		Node node = graph.findNodeByName(activeState);
		if (node != null) {
			node.setAttribute("color", "");
		}
		activeState = name.replace(".", "::");
		node = graph.findNodeByName(activeState);
		if (node != null) {
			node.setAttribute("color", "yellow");
		}
	}
}
