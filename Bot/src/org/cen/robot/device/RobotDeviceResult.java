package org.cen.robot.device;

/**
 * Object representing the result of the treatment of a request by a device of
 * the robot.
 * 
 * @author Emmanuel ZURMELY
 */
public abstract class RobotDeviceResult extends RobotDeviceEvent {
	protected RobotDeviceRequest request;

	/**
	 * Constructor.
	 * 
	 * @param request
	 *            the request associated to this result
	 */
	public RobotDeviceResult(RobotDeviceRequest request) {
		super();
		this.request = request;
	}

	/**
	 * The request corresponding to this result object.
	 * 
	 * @return the request corresponding to this result object
	 */
	public RobotDeviceRequest getRequest() {
		return request;
	}
}
