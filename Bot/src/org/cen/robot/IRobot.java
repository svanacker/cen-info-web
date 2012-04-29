package org.cen.robot;

import java.util.List;

import org.cen.robot.device.IRobotDevice;
import org.springframework.beans.factory.annotation.Required;

/**
 * Interface of the robot object.
 * 
 * @author Emmanuel ZURMELY
 */
public interface IRobot {
	/**
	 * Returns the attribute value coresponding to the given attribute class.
	 * 
	 * @param attributeType
	 *            the type of the robot attribute to retrieve
	 * @return the attribute value
	 */
	public <E extends IRobotAttribute> E getAttribute(Class<E> attributeType);

	/**
	 * Returns the list of the attributes of the robot.
	 * 
	 * @return the list of the attributes
	 */
	public List<IRobotAttribute> getAttributes();

	/**
	 * Returns the configuration of the robot.
	 * 
	 * @return the configuration of the robot
	 */
	public IRobotConfiguration getConfiguration();

	/**
	 * Returns the list of the devices.
	 * 
	 * @return the list of the devices
	 */
	public List<IRobotDevice> getDevices();

	/**
	 * Returns the name of the robot.
	 * 
	 * @return the name of the robot
	 */
	public String getName();

	/**
	 * Sets the configuration of the robot.
	 * 
	 * @param configuration
	 *            the configuration of the robot
	 */
	@Required
	public void setConfiguration(IRobotConfiguration configuration);

	/**
	 * Sets the device list of the robot.
	 * 
	 * @param devices
	 *            the device list of the robot
	 */
	@Required
	void setDevices(List<IRobotDevice> devices);
}
