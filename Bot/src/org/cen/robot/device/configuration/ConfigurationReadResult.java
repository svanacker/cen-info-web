package org.cen.robot.device.configuration;

public class ConfigurationReadResult extends ConfigurationResult {
	private int value;

	public ConfigurationReadResult(ConfigurationRequest request, int value) {
		super(request);
		this.value = value;
	}

	public boolean getSwitch(int index) {
		return (((value >> index) & 1) == 1);
	}

	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[value: " + value + "]";
	}
}
