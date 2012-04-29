package org.cen.robot.device.navigation.analysis;

import org.cen.com.in.InData;

/**
 * Encapsulates value from Data which is used for analyze PID correction.
 */
public class PIDMotionDataInData extends InData {

	public static final String HEADER = "*";

	protected int index;
	protected float pidTime;
	protected float pidType;
	protected float position;
	protected float u;
	protected float errorDataError;
	protected float endInfoTime;
	protected float endInfoAbsDeltaPositionIntegral;
	protected float endInfoUIntegral;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public float getPidTime() {
		return pidTime;
	}

	public void setPidTime(float pidTime) {
		this.pidTime = pidTime;
	}

	public float getPidType() {
		return pidType;
	}

	public void setPidType(float pidType) {
		this.pidType = pidType;
	}

	public float getPosition() {
		return position;
	}

	public void setPosition(float position) {
		this.position = position;
	}

	public float getU() {
		return u;
	}

	public void setU(float u) {
		this.u = u;
	}

	public float getErrorDataError() {
		return errorDataError;
	}

	public void setErrorDataError(float errorDataError) {
		this.errorDataError = errorDataError;
	}

	public float getEndInfoTime() {
		return endInfoTime;
	}

	public void setEndInfoTime(float endInfoTime) {
		this.endInfoTime = endInfoTime;
	}

	public float getEndInfoAbsDeltaPositionIntegral() {
		return endInfoAbsDeltaPositionIntegral;
	}

	public void setEndInfoAbsDeltaPositionIntegral(float endInfoAbsDeltaPositionIntegral) {
		this.endInfoAbsDeltaPositionIntegral = endInfoAbsDeltaPositionIntegral;
	}

	public float getEndInfoUIntegral() {
		return endInfoUIntegral;
	}

	public void setEndInfoUIntegral(float endInfoUIntegral) {
		this.endInfoUIntegral = endInfoUIntegral;
	}

	@Override
	public String toString() {
		return "PIDMotionDataInData [index=" + index + ", pidTime=" + pidTime + ", pidType=" + pidType + ", position=" + position
				+ ", u=" + u + ", errorDataError=" + errorDataError + ", endInfoTime=" + endInfoTime
				+ ", endInfoAbsDeltaPositionIntegral=" + endInfoAbsDeltaPositionIntegral + ", endInfoUIntegral="
				+ endInfoUIntegral + "]";
	}
}
