package org.cen.robot.device;

/**
 * Object representing a request to send to a device of the robot.
 * 
 * @author Emmanuel ZURMELY
 */
public abstract class RobotDeviceRequest {
	private static int counter = 0;

	private static synchronized long getNextUID() {
		return counter++;
	}

	protected String deviceName;

	protected int priority;

	protected long timeStamp;

	protected long uid;

	/**
	 * Constructor.
	 */
	public RobotDeviceRequest(String deviceName) {
		super();
		this.deviceName = deviceName;
		uid = getNextUID();
		priority = 0;
		timeStamp = System.currentTimeMillis();
	}

	/**
	 * Returns the device name.
	 * 
	 * @return the device name
	 */
	public String getDeviceName() {
		return deviceName;
	}

	/**
	 * Returns the priority of the request.
	 * 
	 * @return the priority of the request
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * Returns the time-stamp of this request.
	 * 
	 * @return the time-stamp of this request
	 */
	public long getTimeStamp() {
		return timeStamp;
	}

	long getUID() {
		return uid;
	}
}
