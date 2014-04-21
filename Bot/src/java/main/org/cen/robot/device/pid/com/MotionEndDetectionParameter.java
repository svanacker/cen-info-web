package org.cen.robot.device.pid.com;

/**
 * Encapsulates the parameter to detect the end of motion.
 */
public class MotionEndDetectionParameter {

	/**
	 * Defines the delta position integral for which we consider that below this
	 * value the robot don't move
	 */
	private float absDeltaPositionIntegralFactorThreshold;

	/**
	 * Defines the u integral factor integral for which we consider that there
	 * is a blocking. For example, if the value is equal to 4, it indicates that
	 * if the average integral of U is more than 4x the normal value of u (with
	 * no load), we must consider it as a blocking
	 */
	private float maxUIntegralFactorThreshold;

	/**
	 * When the robot is very low, the answer of the motor is not linear, and we
	 * can thing that the robot is blocked, because, the consign is very high
	 * compared to the normal value. So this value is
	 */
	private float maxUIntegralConstantThreshold;

	/**
	 * TimeRangeAnalysis. It's important to detect on a small range to determine
	 * if the robot is blocked or not (to avoid problems with motors). But too
	 * short range time analysis give sometimes bad analysis. It's also
	 * important to detect on a small range to have a decision of end detection
	 * (to continue on next instruction). But too short range time analysis give
	 * sometimes bad analysis.
	 */
	private int timeRangeAnalysis;

	/**
	 * The delay for which we do not try to check the end detection parameter.
	 * It avoids that the robot stop immediately the begin of motion, because it
	 * consideres that the robot is blocked or has ended his trajectory
	 */
	private int noAnalysisAtStartupTime;

	public MotionEndDetectionParameter() {

	}

	public MotionEndDetectionParameter(float absDeltaPositionIntegralFactorThreshold,
			float maxUIntegralFactorThreshold, float maxUIntegralConstantThreshold, int timeRangeAnalysis,
			int noAnalysisAtStartupTime) {
		super();
		this.absDeltaPositionIntegralFactorThreshold = absDeltaPositionIntegralFactorThreshold;
		this.maxUIntegralFactorThreshold = maxUIntegralFactorThreshold;
		this.maxUIntegralConstantThreshold = maxUIntegralConstantThreshold;
		this.timeRangeAnalysis = timeRangeAnalysis;
		this.noAnalysisAtStartupTime = noAnalysisAtStartupTime;
	}

	public float getAbsDeltaPositionIntegralFactorThreshold() {
		return absDeltaPositionIntegralFactorThreshold;
	}

	public void setAbsDeltaPositionIntegralFactorThreshold(float absDeltaPositionIntegralFactorThreshold) {
		this.absDeltaPositionIntegralFactorThreshold = absDeltaPositionIntegralFactorThreshold;
	}

	public float getMaxUIntegralFactorThreshold() {
		return maxUIntegralFactorThreshold;
	}

	public void setMaxUIntegralFactorThreshold(float maxUIntegralFactorThreshold) {
		this.maxUIntegralFactorThreshold = maxUIntegralFactorThreshold;
	}

	public float getMaxUIntegralConstantThreshold() {
		return maxUIntegralConstantThreshold;
	}

	public void setMaxUIntegralConstantThreshold(float maxUIntegralConstantThreshold) {
		this.maxUIntegralConstantThreshold = maxUIntegralConstantThreshold;
	}

	public int getTimeRangeAnalysis() {
		return timeRangeAnalysis;
	}

	public void setTimeRangeAnalysis(int timeRangeAnalysis) {
		this.timeRangeAnalysis = timeRangeAnalysis;
	}

	public int getNoAnalysisAtStartupTime() {
		return noAnalysisAtStartupTime;
	}

	public void setNoAnalysisAtStartupTime(int noAnalysisAtStartupTime) {
		this.noAnalysisAtStartupTime = noAnalysisAtStartupTime;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[absDeltaPositionIntegralFactorThreshold="
				+ absDeltaPositionIntegralFactorThreshold + ", maxUIntegralFactorThreshold="
				+ maxUIntegralFactorThreshold + ", maxUIntegralConstantThreshold=" + maxUIntegralConstantThreshold
				+ ", timeRangeAnalysis=" + timeRangeAnalysis + ", noAnalysisAtStartupTime=" + noAnalysisAtStartupTime
				+ "]";
	}
}
