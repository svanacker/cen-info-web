package org.cen.robot.match;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PreDestroy;

import org.cen.robot.IRobotServiceInitializable;
import org.cen.robot.IRobotServiceProvider;

public abstract class AbstractMatchStrategy implements IMatchStrategy, IRobotServiceInitializable {

	protected Collection<IMatchStrategyHandler> handlers = new ArrayList<IMatchStrategyHandler>();

	protected IRobotServiceProvider servicesProvider;

	@Override
	public void addHandler(IMatchStrategyHandler handler) {
		handlers.add(handler);
	}

	@Override
	public void afterRegister() {
		ArrayList<IMatchStrategyHandler> h = new ArrayList<IMatchStrategyHandler>(handlers);
		for (IMatchStrategyHandler handler : h) {
			handler.start();
		}
	}

	@Override
	public Collection<IMatchStrategyHandler> getHandlers() {
		return handlers;
	}

	@Override
	public void removeHandler(IMatchStrategyHandler handler) {
		handlers.remove(handler);
	}

	@Override
	public void setServicesProvider(IRobotServiceProvider provider) {
		servicesProvider = provider;
		provider.registerService(IMatchStrategy.class, this);
	}

	@PreDestroy
	public void shutdown() {
		ArrayList<IMatchStrategyHandler> h = new ArrayList<IMatchStrategyHandler>(handlers);
		for (IMatchStrategyHandler handler : h) {
			handler.stop();
		}
	}
}
