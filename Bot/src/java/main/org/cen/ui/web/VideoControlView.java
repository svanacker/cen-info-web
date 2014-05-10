package org.cen.ui.web;

import javax.annotation.PostConstruct;

import org.cen.robot.services.IRobotService;
import org.cen.robot.services.IRobotServiceProvider;
import org.cen.vision.IVisionService;
import org.cen.vision.control.IVideoDeviceControl;
import org.cen.vision.control.VideoDeviceProperty;

public class VideoControlView implements IRobotService {
	private boolean automaticGain;

	private int exposure;

	private int gain;

	private IRobotServiceProvider servicesProvider;

	public boolean getAutomaticGain() {
		return automaticGain;
	}

	public int getExposure() {
		return exposure;
	}

	public int getGain() {
		return gain;
	}

	public IVideoDeviceControl getVideoControl() {
		IVisionService service = servicesProvider.getService(IVisionService.class);
		if (service == null) {
			return null;
		}
		return service.getVideoControl();
	}

	@PostConstruct
	public void initialize() {
		updateValues();
		IVideoDeviceControl control = getVideoControl();
		if (control != null) {
			automaticGain = control.getProperty(VideoDeviceProperty.AutomaticGain) == 0;
		}
	}

	public boolean isAvailable() {
		IVideoDeviceControl control = getVideoControl();
		return (control != null) && control.isAvailable();
	}

	public void setAutomaticGain(boolean automaticGain) {
		if (this.automaticGain == automaticGain) {
			return;
		}
		this.automaticGain = automaticGain;
		setProperty(VideoDeviceProperty.AutomaticGain, (automaticGain) ? -1 : 0);
		if (!automaticGain) {
			updateValues();
		}
	}

	public void setExposure(int exposure) {
		if (this.exposure == exposure) {
			return;
		}
		this.exposure = exposure;
		setProperty(VideoDeviceProperty.Exposure, 1d - (exposure / 100d));
	}

	public void setGain(int gain) {
		if (this.gain == gain) {
			return;
		}
		this.gain = gain;
		setProperty(VideoDeviceProperty.Gain, gain / 100d);
	}

	private void setProperty(VideoDeviceProperty property, double value) {
		IVideoDeviceControl control = getVideoControl();
		if (control != null) {
			control.setProperty(property, value);
		}
	}

	@Override
	public void setServicesProvider(IRobotServiceProvider provider) {
		servicesProvider = provider;
		initialize();
	}

	private void updateValues() {
		IVideoDeviceControl control = getVideoControl();
		if (control != null) {
			gain = (int) (control.getProperty(VideoDeviceProperty.Gain) * 100d);
			exposure = (int) ((1d - control.getProperty(VideoDeviceProperty.Exposure)) * 100d);
		}
	}
}
