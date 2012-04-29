package org.cen.cup.cup2008.robot;

import org.cen.robot.AbstractRobotFactory;
import org.cen.robot.IRobot;

public class RobotFactory2008 extends AbstractRobotFactory {
	@Override
	public Class<?> getObjectType() {
		return Robot2008.class;
	}

	@Override
	protected IRobot newRobotInstance() {
		IRobot robot = new Robot2008();
		return robot;
	}
}
