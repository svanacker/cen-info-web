package org.cen.robot.device.vision;

import org.cen.robot.device.AbstractRobotDevice;
import org.cen.robot.services.IRobotServiceProvider;
import org.cen.vision.IVisionService;

public abstract class AbstractVisionDevice extends AbstractRobotDevice {
	public static final String NAME = "vision";

	protected IVisionService visionService;

	public AbstractVisionDevice() {
		super(NAME);
	}

	@Override
	public void initialize(IRobotServiceProvider servicesProvider) {
		super.initialize(servicesProvider);
		IVisionService visionService = servicesProvider.getService(IVisionService.class);
		initializeVision(visionService);
		this.visionService = visionService;
	}

	protected abstract void initializeVision(IVisionService visionService);
}
