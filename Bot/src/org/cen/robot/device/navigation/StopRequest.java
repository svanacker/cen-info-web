package org.cen.robot.device.navigation;

/**
 * Request for stopping the robot during a movement.
 * 
 * @author Emmanuel ZURMELY
 */
public final class StopRequest extends NavigationRequest {

	/**
	 * Constructor.
	 */
	public StopRequest() {
		super();
		// higher priority
		priority = 10;
	}
}
