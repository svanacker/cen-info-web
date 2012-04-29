package org.cen.vision.util;

import java.awt.Image;
import java.util.EventListener;

import org.cen.vision.filters.CalibrationStat;

/**
 * Interface enabling colour calibration debugging.
 * 
 * @author Emmanuel ZURMELY
 */
public interface CalibrationDebugListener extends EventListener {
	/**
	 * Method called by the calibration process after each analysis of an
	 * acquired image.
	 * 
	 * @param img
	 *            the image acquired
	 * @param stats
	 *            the calibration statistics corresponding to the acquired image
	 */
	public void debug(Image img, CalibrationStat stats);
}
