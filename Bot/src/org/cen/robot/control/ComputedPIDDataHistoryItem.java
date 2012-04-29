package org.cen.robot.control;

/**
 * Encapsulates all computed parameters for a PID correction which is computed
 * and applied to the motors.
 */
public class ComputedPIDDataHistoryItem {

	/** Internal Time for pid (pid is computed x times by seconds. */
	protected float pidTime;

	/** Stores information about errors. */
	protected MotionErrorData err;

	/** Stores information about motion. */
	protected MotionData motion;

	/** Detection of end of trajectory. */
	protected MotionEndInfoData motionEnd;

	public ComputedPIDDataHistoryItem() {
		motionEnd = new MotionEndInfoData();
		err = new MotionErrorData();
		motion = new MotionData();
	}

	public MotionErrorData getErr() {
		return err;
	}

	public void setErr(MotionErrorData err) {
		this.err = err;
	}

	public MotionData getMotion() {
		return motion;
	}

	public void setMotion(MotionData motion) {
		this.motion = motion;
	}

	public MotionEndInfoData getMotionEnd() {
		return motionEnd;
	}

	public void setMotionEnd(MotionEndInfoData motionEnd) {
		this.motionEnd = motionEnd;
	}

	public float getPidTime() {
		return pidTime;
	}

	public void setPidTime(float pidTime) {
		this.pidTime = pidTime;
	}
}
