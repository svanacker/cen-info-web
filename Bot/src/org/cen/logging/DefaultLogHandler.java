package org.cen.logging;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Vector;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class DefaultLogHandler extends Handler {
	private List<LogRecord> records;

	public DefaultLogHandler(List<LogRecord> records) {
		super();
		this.records = records;
	}

	@Override
	public void close() throws SecurityException {
		flush();
	}

	@Override
	public void flush() {
		records.clear();
	}

	@Override
	public void publish(LogRecord record) {
		records.add(record);
	}

	public void save(Writer writer) {
		Formatter formatter = getFormatter();
		if (formatter == null) {
			formatter = new SimpleFormatter();
		}
		Vector<LogRecord> r = new Vector<LogRecord>();
		r.addAll(records);
		try {
			for (LogRecord record : r) {
				writer.write(formatter.format(record));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
