package org.cen.robot.device.navigation.position.com;

import org.cen.com.in.InData;
import org.cen.math.MathUtils;

/**
 * Read position with the following pattern kSS-XXXX-YYYY-TTTT where
 * 
 * <ul>
 * <li>SS = status (@see PositionStatus)
 * <li>XXXX = X in mm</li>
 * <li>, YYYY = Y in mm</li>
 * <li>, TTTT in deciDegree.</li>
 * </ul>
 */
public class PositionInData extends InData {

	public final static String HEADER = "k";

	private final double x;

	private final double y;

	private final double alphaRadian;

	private final PositionStatus status;

	public PositionInData(long x, long y, long alpha, byte status) {
		super();
		this.x = x;
		this.y = y;
		this.alphaRadian = MathUtils.deciDegreeToRad(alpha);
		this.status = PositionStatus.values()[status];
	}

	public double getAlpha() {
		return alphaRadian;
	}

	public PositionStatus getStatus() {
		return status;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[x=" + x + ", y=" + y + ", alpha=" + Math.toDegrees(alphaRadian)
				+ "°, status=" + status + "]";
	}
}