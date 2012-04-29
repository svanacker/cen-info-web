package org.cen.robot.device;

import java.util.EventListener;

public interface RobotDeviceDebugListener extends EventListener {
	public String getDeviceName();

	public void debugEvent(RobotDeviceDebugEvent e);
}
