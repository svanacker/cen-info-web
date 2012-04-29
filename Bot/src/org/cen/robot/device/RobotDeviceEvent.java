package org.cen.robot.device;

/**
 * Event raised by a device of the robot.
 * 
 * @author Emmanuel ZURMELY
 */
public class RobotDeviceEvent {
	protected long timeStamp;

	/**
	 * Constructor.
	 */
	public RobotDeviceEvent() {
		super();
		timeStamp = System.currentTimeMillis();
	}
}
