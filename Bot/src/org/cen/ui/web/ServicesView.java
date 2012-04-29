package org.cen.ui.web;

import java.util.Collection;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.cen.logging.LoggingUtils;
import org.cen.robot.IRobotFactory;
import org.cen.robot.IRobotService;
import org.cen.robot.IRobotServiceInitializable;
import org.cen.robot.IRobotServiceProvider;
import org.cen.util.ReflectionUtils;

public class ServicesView implements IRobotService {
	private IRobotServiceProvider servicesProvider;

	private static final Logger LOGGER = LoggingUtils.getClassLogger();

	@Override
	public void setServicesProvider(IRobotServiceProvider provider) {
		this.servicesProvider = provider;
	}

	public void restart() {
		// TODO utiliser TimeHandler

		LOGGER.info("full restart");
		Collection<IRobotService> services = servicesProvider.getServices();
		for (IRobotService service : services) {
			LOGGER.finest("shutting down " + service.getClass().getSimpleName());
			ReflectionUtils.invoke(PreDestroy.class, service, null);
		}

		LOGGER.finest("restarting robot");
		IRobotFactory factory = servicesProvider.getService(IRobotFactory.class);
		factory.restart();

		for (IRobotService service : services) {
			LOGGER.finest("initializing " + service.getClass().getSimpleName());
			ReflectionUtils.invoke(PostConstruct.class, service, null);
		}

		for (IRobotService service : services) {
			if (service instanceof IRobotServiceInitializable) {
				LOGGER.finest("post registration of " + service.getClass().getSimpleName());
				((IRobotServiceInitializable) service).afterRegister();
			}
		}
	}
}
