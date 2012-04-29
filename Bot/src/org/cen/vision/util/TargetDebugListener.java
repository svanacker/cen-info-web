package org.cen.vision.util;

import java.awt.Image;
import java.util.EventListener;

import org.cen.vision.filters.TargetStat;

public interface TargetDebugListener extends EventListener {
	/**
	 * Method called by the target detection process after each analysis of an
	 * acquired image.
	 * 
	 * @param source
	 *            the target handler source object
	 * @param img
	 *            the image acquired
	 * @param stats
	 *            the statistics about target detection corresponding to the
	 *            acquired image
	 * @param historyGrid
	 *            the history used for target detection
	 */
	public void debug(TargetHandler source, Image img, TargetStat stats, double[][] historyGrid);
}
