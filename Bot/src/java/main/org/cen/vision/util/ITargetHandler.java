package org.cen.vision.util;

import org.cen.robot.services.IRobotService;

public interface ITargetHandler extends IRobotService {
	public TargetHandler getTargetHandler();

	public TargetHandlerThread getTargetHandlerThread();
}
