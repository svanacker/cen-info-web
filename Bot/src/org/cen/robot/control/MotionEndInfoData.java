package org.cen.robot.control;

/**
 * Encapsulates information about end of trajectory. Compute it is necessary to
 * detect
 * <ul>
 * <li>The robot is blocked : the abs of position integral is low, but uIntegral
 * is very high</li>
 * <li>The robot has finished his trajectory : the abs of integral position is
 * small, and uIntegral is very low</li>
 * </ul>
 */
public class MotionEndInfoData {

	/** The time of the integral. */
	protected float time;

	/** The max time of the integral. */
	protected float maxTime;

	/** The integral of the absolute value of delta position. */
	protected float absDeltaPositionIntegral;

	/** The integral of the consign determined by the pid computer */
	protected float uIntegral;

	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		this.time = time;
	}

	public float getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(float maxTime) {
		this.maxTime = maxTime;
	}

	public float getAbsDeltaPositionIntegral() {
		return absDeltaPositionIntegral;
	}

	public void setAbsDeltaPositionIntegral(float absDeltaPositionIntegral) {
		this.absDeltaPositionIntegral = absDeltaPositionIntegral;
	}

	public float getuIntegral() {
		return uIntegral;
	}

	public void setuIntegral(float uIntegral) {
		this.uIntegral = uIntegral;
	}

}
