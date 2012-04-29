package org.cen.robot.control;

/**
 * Encapsulates error, derivativeError, integralError during a compute of PID
 * correction.
 */
public class MotionErrorData {

	/** Stores the previous error */
	protected float previousError;

	/** The error between normal speed and real speed */
	protected float error;

	/** The derivative error between normal speed and real speed */
	protected float derivativeError;

	/** The integral error between normal speed and real speed */
	protected float integralError;

	public MotionErrorData() {

	}

	public float getPreviousError() {
		return previousError;
	}

	public void setPreviousError(float previousError) {
		this.previousError = previousError;
	}

	public float getError() {
		return error;
	}

	public void setError(float error) {
		this.error = error;
	}

	public float getDerivativeError() {
		return derivativeError;
	}

	public void setDerivativeError(float derivativeError) {
		this.derivativeError = derivativeError;
	}

	public float getIntegralError() {
		return integralError;
	}

	public void setIntegralError(float integralError) {
		this.integralError = integralError;
	}
}
