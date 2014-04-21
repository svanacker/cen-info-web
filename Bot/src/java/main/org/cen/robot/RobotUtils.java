package org.cen.robot;

/**
 * Utility Class to get easily resources around Robot. 
 */
public class RobotUtils {
	public static IRobot getRobot(IRobotServiceProvider provider) {
		IRobotFactory factory = provider.getService(IRobotFactory.class);
		return factory.getRobot();
	}

	public static <E extends IRobotAttribute> E getRobotAttribute(Class<E> attributeType, IRobotServiceProvider provider) {
		return getRobot(provider).getAttribute(attributeType);
	}
}
