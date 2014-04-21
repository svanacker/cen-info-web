package org.cen.ui.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Presentation Object for an element of log.
 * @author svanacker
 * @version 2008/03/02
 */
public class LogRecordView {
	
	protected LogRecord logRecord;

	public LogRecordView(LogRecord logRecord) {
		this.logRecord = logRecord;
	}
	
	public String getDateAsString() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(logRecord.getMillis());
		String format = dateFormat.format(calendar.getTime());
		
		return format;
	}
	
	public LogRecord getLogRecord() {
		return logRecord;
	}

	public void setLogRecord(LogRecord logRecord) {
		this.logRecord = logRecord;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[logRecord=" + logRecord + "]";
	}

	public Level getLevel() {
		return logRecord.getLevel();
	}

	public String getLoggerName() {
		return logRecord.getLoggerName();
	}

	public String getMessage() {
		return logRecord.getMessage();
	}

	public long getMillis() {
		return logRecord.getMillis();
	}
}
