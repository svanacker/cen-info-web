package org.cen.vision.logitech;

import static org.cen.vision.logitech.VideoDeviceProperty.VDP_AGC;
import static org.cen.vision.logitech.VideoDeviceProperty.VDP_EXPOSURE_CURRENTPOSITION;
import static org.cen.vision.logitech.VideoDeviceProperty.VDP_EXPOSURE_MAX;
import static org.cen.vision.logitech.VideoDeviceProperty.VDP_EXPOSURE_MIN;
import static org.cen.vision.logitech.VideoDeviceProperty.VDP_GAIN_CURRENTPOSITION;
import static org.cen.vision.logitech.VideoDeviceProperty.VDP_GAIN_MAX;
import static org.cen.vision.logitech.VideoDeviceProperty.VDP_GAIN_MIN;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.cen.logging.LoggingUtils;
import org.cen.vision.control.IVideoDeviceControl;
import org.cen.vision.control.VideoDeviceProperty;

public class QuickCamControl implements IVideoDeviceControl {
	private static final Logger LOGGER = LoggingUtils.getClassLogger();

	private String device;

	private LogitechDeviceManager manager;

	private Map<VideoDeviceProperty, Property> properties;

	public QuickCamControl() {
		super();
		properties = new HashMap<VideoDeviceProperty, Property>();
	}

	private boolean checkManager() {
		if (manager == null) {
			LOGGER.finest("manager not available, operation aborted");
			return false;
		}
		return true;
	}

	@Override
	public String getName() {
		return "Logitech QuickCam video control";
	}

	@Override
	public double getProperty(VideoDeviceProperty property) {
		LOGGER.finer("getting video property " + property);
		if (!checkManager()) {
			return 0d;
		}

		Property p = properties.get(property);
		if (p == null) {
			LOGGER.warning("property " + property.name() + " not registered");
			return 0d;
		}
		if (!p.isInitialized()) {
			LOGGER.finest("initializing characteristics of property " + property);
			initializeProperty(p);
		}
		LOGGER.finest("getting value from device");
		Object value = manager.getVideoDeviceProperty(device, p.getValueIndex());
		if (value instanceof Number) {
			return p.normalize(((Number) value).intValue());
		}
		LOGGER.warning("the value (" + value.toString() + ") returned from the device for the property " + property + " could not be interpreted as a number");
		return 0d;
	}

	@PostConstruct
	public void initialize() {
		LOGGER.config("initializing " + getName());

		try {
			manager = new LogitechDeviceManager();
		} catch (Exception e) {
			LOGGER.warning(e.getMessage());
			return;
		}

		registerProperties();
		manager.initialize("", "");
		manager.enumerateDevices();
		device = manager.getVideoDeviceByIndex(0);
		if (device == null) {
			manager.uninitialize();
			manager = null;
			LOGGER.warning("no device found");
			return;
		}
	}

	private void initializeProperty(Property p) {
		if (p.hasValueRange()) {
			int min = ((Number) manager.getVideoDeviceProperty(device, p.getMinIndex())).intValue();
			int max = ((Number) manager.getVideoDeviceProperty(device, p.getMaxIndex())).intValue();
			p.setRange(min, max);
		} else {
			p.setRange(0, 1);
		}
		LOGGER.finest("property initialized: " + p.toString());
	}

	@Override
	public boolean isAvailable() {
		return (manager != null);
	}

	private void registerProperties() {
		properties.put(VideoDeviceProperty.Exposure, new Property(VDP_EXPOSURE_CURRENTPOSITION, VDP_EXPOSURE_MIN, VDP_EXPOSURE_MAX));
		properties.put(VideoDeviceProperty.Gain, new Property(VDP_GAIN_CURRENTPOSITION, VDP_GAIN_MIN, VDP_GAIN_MAX));
		properties.put(VideoDeviceProperty.AutomaticGain, new Property(VDP_AGC));
	}

	@Override
	public void setProperty(VideoDeviceProperty property, double value) {
		LOGGER.finer("setting video property " + property);
		if (!checkManager()) {
			return;
		}

		Property p = properties.get(property);
		if (p == null) {
			LOGGER.warning("property " + property.name() + " not registered");
			return;
		}
		if (!p.isInitialized()) {
			LOGGER.finest("initializing characteristics of property " + property);
			initializeProperty(p);
		}
		int newValue = p.denormalize(value);
		LOGGER.finest("setting new value to device: " + value + "(" + newValue + ")");
		manager.setVideoDeviceProperty(device, p.getValueIndex(), newValue);
	}

	@PreDestroy
	public void shutdown() {
		LOGGER.config("shutting down " + getName());
		if (manager != null) {
			manager.close();
		}
	}
}
