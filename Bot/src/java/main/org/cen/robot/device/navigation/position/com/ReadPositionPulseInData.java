package org.cen.robot.device.navigation.position.com;

import org.cen.com.in.InData;

/**
 * Read data which give the position of the both wheels with the following
 * pattern: wXXXXXXXX-YYYYYYYY where
 * <ul>
 * <li>XXXXXXXX is an absolute position in pulse for left Wheel</li>
 * <li>YYYYYYYY is an absolute position in pulse for left Wheel</li>
 * </ul>
 */
public class ReadPositionPulseInData extends InData {

	public final static String HEADER = "w";

	public final static int LENGTH_WITHOUT_HEADER = 17;

	public final static int LENGTH_WITH_HEADER = 18;

	public final static int LENGTH_LEFT = 8;
	public final static int LENGTH_RIGHT = 8;

	private final long left;

	private final long right;

	public ReadPositionPulseInData(long left, long right) {
		super();
		this.left = left;
		this.right = right;
	}

	/**
	 * @return the left
	 */
	public long getLeft() {
		return left;
	}

	/**
	 * @return the right
	 */
	public long getRight() {
		return right;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[left=" + left + ", right=" + right + "]";
	}
}
