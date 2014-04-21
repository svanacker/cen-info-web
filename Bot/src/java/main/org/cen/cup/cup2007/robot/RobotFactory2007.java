package org.cen.cup.cup2007.robot;

import org.cen.robot.AbstractRobotFactory;
import org.cen.robot.IRobot;

/**
 Implementation of the eurobot cup 2007 edition. 
 * @author svanacker
 */
public class RobotFactory2007 extends AbstractRobotFactory {
	@Override
	public Class<?> getObjectType() {
		return Robot2007.class;
	}

	@Override
	protected IRobot newRobotInstance() {
		return new Robot2007();
	}
}
