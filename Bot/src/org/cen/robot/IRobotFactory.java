package org.cen.robot;

/**
 * Interface of the robot factory. The robot factory is responsible for the
 * specific initialization of a IRobot instance.
 * 
 * @author Emmanuel ZURMELY
 * 
 */
public interface IRobotFactory extends IRobotService {
	/**
	 * Returns the robot object.
	 * 
	 * @return the robot object
	 */
	public IRobot getRobot();

	/**
	 * Returns the robot configuration.
	 * 
	 * @return the robot configuration
	 */
	public IRobotConfiguration getRobotConfiguration();

	/**
	 * Restarts the robot.
	 */
	public void restart();

	/**
	 * Sets the robot configuration.
	 * 
	 * @param configuration
	 *            the configuration interface to set
	 */
	public void setRobotConfiguration(IRobotConfiguration configuration);
}