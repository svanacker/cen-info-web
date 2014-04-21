package org.cen.robot.device.triangulation;

import org.cen.robot.device.RobotDeviceRequest;
import org.cen.robot.device.RobotDeviceResult;

/**
 * Result object to a triangulation request.
 * 
 * @author Emmanuel ZURMELY
 */
public final class TriangulationResult extends RobotDeviceResult {
	/**
	 * Termination status of the triangulation operation.
	 * 
	 * @author Emmanuel ZURMELY
	 */
	public enum TriangulationResultStatus {
		FAILED, SUCCEEDED;
	}

	private final TriangulationResultStatus status;

	/**
	 * Constructor.
	 * 
	 * @param request
	 *            the request object associated to this result
	 * @param status
	 *            the status of the triangulation operation
	 */
	public TriangulationResult(RobotDeviceRequest request, TriangulationResultStatus status) {
		super(request);
		this.status = status;
	}

	/**
	 * Returns the status of the triangulation operation.
	 * 
	 * @return the status of the triangulation operation
	 */
	public TriangulationResultStatus getStatus() {
		return status;
	}
}
