package org.cen.robot.device.triangulation.com;

import org.cen.com.in.InData;

public final class ReadTriangulationInData extends InData {

	static final String HEADER = "l";

	final private int currentPosition;

	final private int lastPosition;

	final private int previousPosition;

	public ReadTriangulationInData(int currentPosition, int lastPosition, int previousPosition) {
		super();
		this.currentPosition = currentPosition;
		this.lastPosition = lastPosition;
		this.previousPosition = previousPosition;
	}

	/**
	 * @return the currentPosition
	 */
	public int getCurrentPosition() {
		return currentPosition;
	}

	/**
	 * @return the lastPosition
	 */
	public int getLastPosition() {
		return lastPosition;
	}

	/**
	 * @return the previousPosition
	 */
	public int getPreviousPosition() {
		return previousPosition;
	}
}
