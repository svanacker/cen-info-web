package org.cen.vision.filters;

/**
 * Statistics object provided by the calibration filter. This object stores
 * informations about the mean color and the variation of an image sample. This
 * object is used to calibrate the color filter.
 * 
 * @author Emmanuel ZURMELY
 */
public class CalibrationStat {
	/**
	 * Statistics data of sub-sample of the image.
	 * 
	 * @author Emmanuel ZURMELY
	 */
	public class Sample {
		private double angle;

		private double distance;

		/**
		 * Constructor.
		 * 
		 * @param angle
		 *            mean color angle of the sub-sample
		 * @param distance
		 *            variation of the color angle
		 */
		public Sample(double angle, double distance) {
			super();
			this.angle = angle;
			this.distance = distance;
		}

		/**
		 * Returns the mean angle.
		 * 
		 * @return the mean angle
		 */
		public double getAngle() {
			return angle;
		}

		/**
		 * Returns the variation of the color angle.
		 * 
		 * @return the variation of the color angle
		 */
		public double getDistance() {
			return distance;
		}

		/**
		 * Sets the mean angle.
		 * 
		 * @param angle
		 *            the mean angle
		 */
		public void setAngle(double angle) {
			this.angle = angle;
		}

		/**
		 * Sets the variation of the color angle.
		 * 
		 * @param distance
		 *            the variation of the color angle
		 */
		public void setDistance(double distance) {
			this.distance = distance;
		}

		@Override
		public String toString() {
			return "a=" + angle + "; d=" + distance;
		}
	}

	private Sample[] samples;

	/**
	 * Returns the sub-samples array for this statistics.
	 * 
	 * @return the sub-samples array
	 */
	public Sample[] getSamples() {
		return samples;
	}

	/**
	 * Sets the sub-samples array of this statistics object.
	 * 
	 * @param samples
	 *            the sub-samples array
	 */
	public void setSamples(Sample[] samples) {
		this.samples = samples;
	}
}
