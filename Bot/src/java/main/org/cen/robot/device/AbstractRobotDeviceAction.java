package org.cen.robot.device;

import java.io.Serializable;

import org.cen.robot.device.console.IRobotDeviceConsoleAction;

public abstract class AbstractRobotDeviceAction implements IRobotDeviceConsoleAction, Serializable {
	private static final long serialVersionUID = 1L;

	private String label;

	private String name;

	public AbstractRobotDeviceAction(String name, String label) {
		super();
		this.name = name;
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public String getName() {
		return name;
	}
}
