package org.cen.vision.util;

import org.cen.robot.services.IRobotServiceProvider;

public class TargetHandlerService implements ITargetHandler {
	private IRobotServiceProvider servicesProvider;

	private TargetHandler targetHandler;

	private TargetHandlerThread thread;

	public TargetHandlerService(TargetHandler targetHandler, TargetHandlerThread thread) {
		super();
		this.targetHandler = targetHandler;
		this.thread = thread;
	}

	@Override
	public TargetHandler getTargetHandler() {
		return targetHandler;
	}

	@Override
	public TargetHandlerThread getTargetHandlerThread() {
		return thread;
	}

	@Override
	public void setServicesProvider(IRobotServiceProvider provider) {
		servicesProvider = provider;
		servicesProvider.registerService(ITargetHandler.class, this);
	}
}
