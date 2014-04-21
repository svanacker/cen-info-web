package org.cen.robot;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

public class RobotServiceProvider implements IRobotServiceProvider {
	private Map<Class<? extends IRobotService>, IRobotService> services;

	public RobotServiceProvider() {
		super();
		services = new HashMap<Class<? extends IRobotService>, IRobotService>();
	}

	@Override
	public <E extends IRobotService> E getService(Class<E> serviceType) {
		IRobotService s = services.get(serviceType);
		return serviceType.cast(s);
	}

	@Override
	public Set<IRobotService> getServices() {
		Set<IRobotService> s = new HashSet<IRobotService>();
		s.addAll(services.values());
		return s;
	}

	@PostConstruct
	public void initialize() {
		// Initializes all the registered services
		for (IRobotService service : services.values()) {
			if (service instanceof IRobotServiceInitializable) {
				((IRobotServiceInitializable) service).afterRegister();
			}
		}
	}

	@Override
	public <E extends IRobotService> void registerService(Class<E> serviceType, E service) {
		services.put(serviceType, service);
	}

	public void setServices(Set<IRobotService> services) {
		// Forces all the services to register themselves
		for (IRobotService service : services) {
			service.setServicesProvider(this);
		}
	}
}
