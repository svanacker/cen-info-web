package org.cen.robot.device;

import java.util.logging.Logger;

import org.cen.logging.LoggingUtils;
import org.cen.robot.IRobotServiceProvider;

/**
 * Factory to creates the devices attached to the robot.
 */
public class RobotDeviceFactory {

	private final IRobotServiceProvider servicesProvider;

	public RobotDeviceFactory(IRobotServiceProvider servicesProvider) {
		super();
		this.servicesProvider = servicesProvider;
	}

	private final static Logger LOGGER = LoggingUtils.getClassLogger();

	/**
	 * Creates a new Instance of Device
	 * 
	 * @param name
	 *            shortName (delete org.cen. base Path), and do not include
	 *            "Device", because it uses Some Convention to find the Device.
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public IRobotDevice getNewInstance(String name) throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		String className = "org.cen." + name + "Device";
		LOGGER.finest("instancing device " + className);
		IRobotDevice device = (IRobotDevice) Class.forName(className).newInstance();
		LOGGER.finest("initializing device " + name);
		device.initialize(servicesProvider);
		LOGGER.finest("device " + name + " initialized");
		return device;
	}
}
