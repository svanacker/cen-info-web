package org.cen.robot;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.cen.logging.LoggingUtils;
import org.cen.robot.IRobot;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.device.IRobotDevice;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.RobotDeviceFactory;
import org.cen.robot.impl.AbstractRobot;
import org.cen.utils.ReflectionUtils;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public abstract class AbstractRobotFactory extends AbstractFactoryBean implements ResourceLoaderAware, IRobotFactory {

	private String configuration;

	private ResourceLoader resourceLoader;

	protected IRobotConfiguration robotConfiguration;

	protected IRobotServiceProvider servicesProvider;

	protected AbstractRobotFactory() {
		super();
	}

	@Override
	protected Object createInstance() throws Exception {
		IRobot robot = newRobotInstance();
		readConfiguration(robot);
		registerDevices(robot);
		initializeRobot(robot);
		ReflectionUtils.invoke(PostConstruct.class, robot, null);
		return robot;
	}

	public String getConfiguration() {
		return configuration;
	}

	@Override
	public IRobot getRobot() {
		try {
			return (IRobot) getObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public IRobotConfiguration getRobotConfiguration() {
		return robotConfiguration;
	}

	protected void initializeAttributes(IRobot robot) {
		// Implémentation par défaut
		AbstractRobot r = (AbstractRobot) robot;
		r.addAttribute(RobotPosition.class, new RobotPosition(0, 0, 0));
	}

	public void initializeRobot(IRobot robot) {
		initializeAttributes(robot);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	protected abstract IRobot newRobotInstance();

	protected void readConfiguration(IRobot robot) {
		robotConfiguration = robot.getConfiguration();
		if (robotConfiguration == null) {
			robotConfiguration = new RobotConfiguration();
			robot.setConfiguration(robotConfiguration);
		}
		if (resourceLoader == null) {
			return;
		}
		Resource r = resourceLoader.getResource(getConfiguration());
		if (r == null) {
			return;
		}
		InputStream is = null;
		try {
			is = r.getInputStream();
			robotConfiguration.getProperties().load(is);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected void registerDevices(IRobot robot) {
		ArrayList<IRobotDevice> devices = new ArrayList<IRobotDevice>();
		IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
		RobotDeviceFactory factory = new RobotDeviceFactory(servicesProvider);

		IRobotConfiguration c = robot.getConfiguration();
		if (c == null) {
			return;
		}
		String devicesNames = c.getProperty(AbstractRobot.KEY_DEVICES);
		String[] names = devicesNames.split(",\\W*");
		for (String name : names) {
			IRobotDevice device;
			try {
				device = factory.getNewInstance(name);
				devices.add(device);
				handler.registerDevice(device);
			} catch (Exception e) {
				LoggingUtils.getClassLogger().throwing(factory.getClass().getSimpleName(), "getNewInstance", e);
				e.printStackTrace();
			}
		}

		robot.setDevices(devices);
	}

	@Override
	public void restart() {
		initializeRobot(getRobot());
	}

	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}

	@Override
	public void setResourceLoader(ResourceLoader loader) {
		resourceLoader = loader;
	}

	@Override
	public void setRobotConfiguration(IRobotConfiguration robotConfiguration) {
		this.robotConfiguration = robotConfiguration;
	}

	@Override
	public void setServicesProvider(IRobotServiceProvider provider) {
		servicesProvider = provider;
		provider.registerService(IRobotFactory.class, this);
	}
}
