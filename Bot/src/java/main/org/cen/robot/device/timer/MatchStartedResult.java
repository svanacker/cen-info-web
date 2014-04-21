package org.cen.robot.device.timer;

import org.cen.robot.device.RobotDeviceRequest;

/**
 * Result object indicating that the match has been started.
 * 
 * @author Emmanuel ZURMELY
 */
public class MatchStartedResult extends TimerResult {
	/**
	 * Constructor.
	 * 
	 * @param request
	 *            the request object of which this object is the result
	 */
	public MatchStartedResult(RobotDeviceRequest request) {
		super(request);
	}
}
