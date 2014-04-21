package org.cen.robot.device.navigation.position.com;

import org.cen.com.in.InData;
import org.cen.math.MathUtils;

/**
 * Read position with the following pattern hXXXX-YYYY-TTTT where Warning. Be
 * CAREFUL to distinguish PositionInData which is sent automatically by the
 * micro-controller without asking him (at the end of the trajectory) and the
 * absolute position which can be asked at any time.
 * <ul>
 * <li>XXXX = X in mm</li>
 * <li>, YYYY = Y in mm</li>
 * <li>, TTTT in deciDegree.</li>
 * </ul>
 */
public class PositionAskInData extends InData {

	public final static String HEADER = "h";

	private final double x;

	private final double y;

	private final double alphaRadian;

	public PositionAskInData(long x, long y, long alpha) {
		super();
		this.x = x;
		this.y = y;
		this.alphaRadian = MathUtils.deciDegreeToRad(alpha);
	}

	public double getAlpha() {
		return alphaRadian;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[x=" + x + ", y=" + y + ", alpha=" + Math.toDegrees(alphaRadian) + "° ]";
	}
}