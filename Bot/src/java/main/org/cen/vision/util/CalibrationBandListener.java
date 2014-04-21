package org.cen.vision.util;

import java.util.EventListener;

import org.cen.vision.dataobjects.CalibrationData;

/**
 * Interface enabling calibration debugging while bands are found.
 * 
 * @author svanacker
 */
public interface CalibrationBandListener extends EventListener {
	/**
	 * Method called by the calibration process after each calibration data is
	 * found.
	 * 
	 * @param calibrationData
	 *            the image acquired
	 */
	public void bandFound(CalibrationData calibrationData);
}
