package org.cen.math;

public final class MathUtils {

	/**
	 * Determines whether the given value is in the specified range.
	 * 
	 * @param d
	 *            the value
	 * @param start
	 *            the range start
	 * @param end
	 *            the range end
	 * @return (d >= start && d <= end)
	 */
	public static boolean isInRange(double d, double start, double end) {
		return (d >= start && d <= end);
	}

	/**
	 * Determines whether the given value is in the specified range.
	 * 
	 * @param f
	 *            the value
	 * @param start
	 *            the range start
	 * @param end
	 *            the range end
	 * @return (d >= start && d <= end)
	 */
	public static boolean isInRange(float f, float start, float end) {
		return (f >= start && f <= end);
	}

	/**
	 * Determines whether two value can be assumed to be the same with a given
	 * error value of epsilon.
	 * 
	 * @param d1
	 *            first value
	 * @param d2
	 *            second value
	 * @param epsilon
	 *            the error
	 * @return TRUE if d1 = d2 +/- epsilon, FALSE otherwise
	 */
	public static boolean isSameValue(double d1, double d2, double epsilon) {
		double d = d1 - d2;
		return (d >= -epsilon && d <= epsilon);
	}

	/**
	 * Determines whether two value can be assumed to be the same with a given
	 * error value of epsilon.
	 * 
	 * @param f1
	 *            first value
	 * @param f2
	 *            second value
	 * @param epsilon
	 *            the error
	 * @return TRUE if f1 = f2 +/- epsilon, FALSE otherwise
	 */
	public static boolean isSameValue(float f1, float f2, float epsilon) {
		float f = f1 - f2;
		return (f >= -epsilon && f <= epsilon);
	}

	/**
	 * Convert a value in deci-degree into milliRadians.
	 * 
	 * @param deciDegree
	 *            a value in decidegree
	 * @return
	 */
	public static double deciDegreeToRad(double deciDegree) {
		double result = deciDegree * (Math.PI / 1800d);
		return result;
	}

	/**
	 * Convert a value in deci-degree into degree.
	 * 
	 * @param deciDegree
	 *            a value in degree
	 * @return
	 */
	public static double deciDegreeToDegree(double deciDegree) {
		double result = deciDegree / 10d;
		return result;
	}

	/**
	 * Convert a value in degree into deci-degree.
	 * 
	 * @param degree
	 *            a value in degree
	 * @return
	 */
	public static double degreeToDeciDegree(double deciDegree) {
		double result = deciDegree * 10d;
		return result;
	}

	/**
	 * Convert a value in radians into deci-degree.
	 * 
	 * @param rad
	 *            a value in milliRadians
	 * @return
	 */
	public static double radToDeciDegree(double angleRad) {
		double result = 1800d / Math.PI * angleRad;
		return result;
	}
}
