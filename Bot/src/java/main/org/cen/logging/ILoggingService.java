package org.cen.logging;

import java.io.Writer;
import java.util.List;
import java.util.logging.LogRecord;

import org.cen.robot.IRobotService;

/**
 * Interface of a service providing global logging features.
 */
public interface ILoggingService extends IRobotService {
	
	/**
	 * Clears the log.
	 */
	public void clear();

	/**
	 * Returns the list of the log records.
	 * 
	 * @return the list of the log records
	 */
	public List<LogRecord> getLogRecords();

	/**
	 * Saves the log records and flushes the list.
	 * 
	 * @param writer
	 *            the write to use for writing the records list
	 */
	public void save(Writer writer);
}
