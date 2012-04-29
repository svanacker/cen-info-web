package org.cen.ui.web;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConsoleView implements Serializable {
	private Map<String, String> actions = new HashMap<String, String>();

	private String name;

	private Properties properties;

	public Properties getProperties() {
		return properties;
	}

	public ConsoleView(String name) {
		super();
		this.name = name;
	}

	public void addAction(String command, String label) {
		actions.put(command, label);
	}

	public Map<String, String> getActions() {
		return actions;
	}

	public String getName() {
		return name;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
}
