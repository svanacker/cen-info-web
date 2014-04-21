package org.cen.logging;

import java.util.logging.Logger;

import org.cen.util.ReflectionUtils;

/**
 * Helper class for logging-related functions.
 */
public class LoggingUtils {

	/**
	 * Return the logger associated with the class from which the call to this
	 * method is made.
	 * 
	 * @return the logger associated with the caller class
	 */
	public static final Logger getClassLogger() {
		String name = ReflectionUtils.getCallerClassName(1);
		return Logger.getLogger(name);
	}
}
