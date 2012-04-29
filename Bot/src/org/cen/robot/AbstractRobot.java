package org.cen.robot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cen.robot.device.IRobotDevice;

/**
 * Abstract implementation of the robot.
 * 
 * @author Emmanuel ZURMELY
 */
public abstract class AbstractRobot implements IRobot {

	public static final String KEY_DEVICES = "devices";

	private final Map<Class<? extends IRobotAttribute>, IRobotAttribute> attributes;

	private IRobotConfiguration configuration;

	protected List<IRobotDevice> devices;

	protected AbstractRobot() {
		super();
		attributes = new HashMap<Class<? extends IRobotAttribute>, IRobotAttribute>();
	}

	public <E extends IRobotAttribute> void addAttribute(Class<E> type, E attribute) {
		attributes.put(type, attribute);
	}

	@Override
	public <E extends IRobotAttribute> E getAttribute(Class<E> attributeType) {
		IRobotAttribute attribute = attributes.get(attributeType);
		return attributeType.cast(attribute);
	}

	@Override
	public List<IRobotAttribute> getAttributes() {
		return new ArrayList<IRobotAttribute>(attributes.values());
	}

	@Override
	public IRobotConfiguration getConfiguration() {
		return configuration;
	}

	@Override
	public List<IRobotDevice> getDevices() {
		return devices;
	}

	@Override
	public void setConfiguration(IRobotConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public void setDevices(List<IRobotDevice> devices) {
		this.devices = devices;
	}
}
