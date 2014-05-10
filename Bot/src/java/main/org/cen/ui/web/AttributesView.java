package org.cen.ui.web;

import java.util.List;

import org.cen.robot.attributes.IRobotAttribute;
import org.cen.robot.services.IRobotServiceProvider;
import org.cen.robot.utils.RobotUtils;

public class AttributesView {
	private IRobotServiceProvider servicesProvider;

	public List<IRobotAttribute> getAttributes() {
		return RobotUtils.getRobot(servicesProvider).getAttributes();
	}

	public void setServicesProvider(final IRobotServiceProvider provider) {
		servicesProvider = provider;
	}
}
