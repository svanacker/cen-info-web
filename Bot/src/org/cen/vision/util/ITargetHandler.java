package org.cen.vision.util;

import org.cen.robot.IRobotService;

public interface ITargetHandler extends IRobotService {
	public TargetHandler getTargetHandler();

	public TargetHandlerThread getTargetHandlerThread();
}
