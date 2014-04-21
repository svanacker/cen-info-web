package org.cen.robot.device;

import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulation of a Device for the robot
 * 
 * @author Stephane
 * @version 23/02/2007
 */
public abstract class RobotDevice {
	/**
	 * The name of the device.
	 */
	protected String name;

	/**
	 * Properties associated to the device.
	 */
	protected Map<String, Object> properties = new HashMap<String, Object>();

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            the name of the device
	 */
	public RobotDevice(String name) {
		super();
		this.name = name;
	}

	/**
	 * Ensures that the specified property has been defined.
	 * 
	 * @param name 
	 *            the name of the property
	 */
	protected void assertPropertyDefined(String name) {
		assert getProperty(name) != null : "Property " + name + " not defined :";
	}

	/**
	 * Executes the specified action for debugging purpose.
	 * 
	 * @param debugAction
	 *            the name of the action
	 */
	public void debug(String debugAction) {
		// no default action
	}

	/**
	 * Returns the name of this device
	 * 
	 * @return the name of this device
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the value of a property of this device.
	 * 
	 * @param name
	 *            the name of the property
	 * @return the value of the property
	 */
	public Object getProperty(String name) {
		return properties.get(name);
	}

	/**
	 * Handles the specified request
	 * 
	 * @param request
	 *            the request object to handle
	 */
	public void handleRequest(RobotDeviceRequest request) {
		internalHandleRequest(request);
	}

	/**
	 * Handles a request.
	 * 
	 * @param request
	 *            the request object to handle
	 */
	protected abstract void internalHandleRequest(RobotDeviceRequest request);

	/**
	 * Sets the value of a property of this device.
	 * 
	 * @param name
	 *            the name of the property
	 * @param value
	 *            the value of the property
	 */
	public void setProperty(String name, Object value) {
		properties.put(name, value);
	}
}
