package org.cen.robot.brain;

import java.util.logging.Logger;

import org.cen.logging.LoggingUtils;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.RobotDeviceListener;

public abstract class AbstractDeviceHandler implements RobotDeviceListener {

	protected IRobotServiceProvider servicesProvider;

	private static final Logger LOGGER = LoggingUtils.getClassLogger();

	/**
	 * Constructor.
	 * 
	 * @param servicesProvider
	 */
	public AbstractDeviceHandler(IRobotServiceProvider servicesProvider) {
		super();
		LOGGER.config("initializing handler " + getClass().getSimpleName());
		this.servicesProvider = servicesProvider;
		IRobotDevicesHandler handler = servicesProvider
				.getService(IRobotDevicesHandler.class);
		handler.addDeviceListener(this);
	}

	/**
	 * Shuts down the handler and unregisters the listener.
	 */
	public void shutdown() {
		LOGGER.config("shuting down handler " + getClass().getSimpleName());
		IRobotDevicesHandler handler = servicesProvider
				.getService(IRobotDevicesHandler.class);
		handler.removeDeviceListener(this);
	}
}
