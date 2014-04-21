package org.cen.robot.device.timer;

import org.cen.robot.device.RobotDeviceRequest;

/**
 * Result object indicating that the match is finished.
 * 
 * @author Emmanuel ZURMELY
 */
public class MatchFinishedResult extends TimerResult {
	/**
	 * Constructor.
	 * 
	 * @param request
	 *            the request of which this object is the result
	 */
	public MatchFinishedResult(RobotDeviceRequest request) {
		super(request);
	}
}
