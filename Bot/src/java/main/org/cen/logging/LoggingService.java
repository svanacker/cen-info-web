package org.cen.logging;

import java.io.Writer;
import java.util.List;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.cen.robot.IRobotServiceProvider;

/**
 * Default implementation for the logging service. This implementation
 * initializes a default logger for the package org.cen
 * 
 * @author Emmanuel ZURMELY
 */
public class LoggingService implements ILoggingService {
	private DefaultLogHandler handler;

	/** The list which stores the log records. */
	private List<LogRecord> records = new Vector<LogRecord>();

	public LoggingService() {
		super();
		handler = new DefaultLogHandler(records);
		Logger logger = Logger.getLogger("org.cen");
		logger.setLevel(Level.FINEST);
		logger.addHandler(handler);

		LoggingUtils.getClassLogger().finest("Logger initialized");
	}

	@Override
	public void clear() {
		records.clear();
	}

	public List<LogRecord> getLogRecords() {
		Vector<LogRecord> v = new Vector<LogRecord>();
		v.addAll(records);
		return v;
	}

	public void save(Writer writer) {
		handler.save(writer);
	}

	public void setFilePattern(String pattern) {
		try {
			FileHandler fileHandler = new FileHandler(pattern, 256000, 1, true);
			fileHandler.setFormatter(new SimpleFormatter());
			Logger logger = Logger.getLogger("org.cen");
			logger.addHandler(fileHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setServicesProvider(IRobotServiceProvider provider) {
		provider.registerService(ILoggingService.class, this);
	}
}
