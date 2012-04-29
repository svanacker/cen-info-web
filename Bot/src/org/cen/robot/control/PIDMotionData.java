package org.cen.robot.control;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Encapsulates all data linked to PID for a motion.
 */
public class PIDMotionData {

	/** Date of object creation. */
	protected Date creationDate;

	/**
	 * theta error (distance for normal trajectory, and along Y axis for Spline
	 * Curve)
	 */
	protected float thetaError;

	/** angle error. */
	protected float alphaError;

	/**
	 * theta error (only for Curve implementation) determine the distance
	 * between normal trajectory tangent line and real trajectory tangent line
	 * (=> X Axis)
	 */
	protected float thetaXAxisError;

	/** store the time of the pid timer. */
	protected float pidTime;

	/**
	 * History of all data computed by the microcontroller during motion (for
	 * each PID).
	 */
	protected PIDDataHistory[] pids;

	public PIDDataHistory[] getDataHistory() {
		return pids;
	}

	public PIDMotionData() {
		creationDate = new Date();
		pids = new PIDDataHistory[PIDInstructionType.COUNT];
		for (int i = 0; i < PIDInstructionType.COUNT; i++) {
			pids[i] = new PIDDataHistory();
		}
	}

	public float getThetaError() {
		return thetaError;
	}

	public void setThetaError(float thetaError) {
		this.thetaError = thetaError;
	}

	public float getAlphaError() {
		return alphaError;
	}

	public void setAlphaError(float alphaError) {
		this.alphaError = alphaError;
	}

	public float getThetaXAxisError() {
		return thetaXAxisError;
	}

	public void setThetaXAxisError(float thetaXAxisError) {
		this.thetaXAxisError = thetaXAxisError;
	}

	public float getPidTime() {
		return pidTime;
	}

	public void setPidTime(float pidTime) {
		this.pidTime = pidTime;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getLabel() {
		SimpleDateFormat format = new SimpleDateFormat("HH-mm-ss SSS");
		Date creationDate = getCreationDate();
		String result = format.format(creationDate);

		return result;
	}
}
