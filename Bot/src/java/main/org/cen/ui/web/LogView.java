package org.cen.ui.web;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

import org.cen.logging.ILoggingService;
import org.cen.robot.services.IRobotServiceProvider;

/**
 * Presentation object which encapsulates the log from the system.
 */
public class LogView {
	private IRobotServiceProvider servicesProvider;

	public void clear() {
		ILoggingService logging = getLoggingService();
		logging.clear();
	}

	private ILoggingService getLoggingService() {
		return servicesProvider.getService(ILoggingService.class);
	}

	public List<LogRecordView> getRecords() {
		ILoggingService logging = getLoggingService();
		List<LogRecord> logRecords = logging.getLogRecords();
		List<LogRecordView> logRecordViews = new ArrayList<LogRecordView>();
		for (LogRecord logRecord : logRecords) {
			LogRecordView logRecordView = new LogRecordView(logRecord);
			logRecordViews.add(logRecordView);
		}
		return logRecordViews;
	}

	public void setServicesProvider(final IRobotServiceProvider provider) {
		servicesProvider = provider;
	}
}
