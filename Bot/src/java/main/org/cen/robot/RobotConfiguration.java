package org.cen.robot;

import java.util.Properties;

public class RobotConfiguration implements IRobotConfiguration {
	private Properties properties;

	public RobotConfiguration() {
		super();
		properties = new Properties();
	}

	public Properties getProperties() {
		return properties;
	}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public void setProperty(String key, String value) {
		properties.setProperty(key, value);
	}
}
