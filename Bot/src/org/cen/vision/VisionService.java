package org.cen.vision;

import java.awt.Image;
import java.awt.image.renderable.RenderedImageFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.media.Format;
import javax.media.jai.JAI;
import javax.media.jai.OperationRegistry;
import javax.media.jai.registry.RIFRegistry;

import org.cen.logging.LoggingUtils;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.RobotUtils;
import org.cen.vision.control.IVideoDeviceControl;
import org.cen.vision.dataobjects.CalibrationDescriptor;
import org.cen.vision.dataobjects.WebCamProperties;
import org.cen.vision.filters.CalibrationFilterDescriptor;
import org.cen.vision.filters.ColorFilterDescriptor;
import org.cen.vision.filters.GrowingRegionDescriptor;
import org.cen.vision.filters.TargetFilterDescriptor;

public class VisionService implements IVisionService {
	private static final Logger LOGGER = LoggingUtils.getClassLogger();

	static {
		registerOperators();
	}

	private static void registerOperators() {
		// TODO améliorer l'enregistrement des opérateurs JAI

		String productName = "Bot";
		OperationRegistry or = JAI.getDefaultInstance().getOperationRegistry();

		ColorFilterDescriptor colorFilterDescriptor = new ColorFilterDescriptor();
		String operationName = "ColorFilter";
		RenderedImageFactory rif = colorFilterDescriptor;
		or.registerDescriptor(colorFilterDescriptor);
		RIFRegistry.register(or, operationName, productName, rif);

		TargetFilterDescriptor targetFilterDescriptor = new TargetFilterDescriptor();
		operationName = "TargetFilter";
		rif = targetFilterDescriptor;
		or.registerDescriptor(targetFilterDescriptor);
		RIFRegistry.register(or, operationName, productName, rif);

		CalibrationFilterDescriptor calibrationFilterDescriptor = new CalibrationFilterDescriptor();
		operationName = "CalibrationFilter";
		rif = calibrationFilterDescriptor;
		or.registerDescriptor(calibrationFilterDescriptor);
		RIFRegistry.register(or, operationName, productName, rif);

		GrowingRegionDescriptor growingRegionFilterDescriptor = new GrowingRegionDescriptor();
		operationName = "GrowingRegionFilter";
		rif = growingRegionFilterDescriptor;
		or.registerDescriptor(growingRegionFilterDescriptor);
		RIFRegistry.register(or, operationName, productName, rif);
	}

	protected Acquisition acquisition;

	protected CalibrationDescriptor descriptor;

	protected IRobotServiceProvider servicesProvider;

	private IVideoDeviceControl videoControl;

	private boolean enabled = true;

	@Override
	public Format[] getAvailableFormats() {
		return acquisition.getAvailableFormats();
	}

	@Override
	public CalibrationDescriptor getCalibrationDescriptor() {
		return descriptor;
	}

	@Override
	public boolean getEnabled() {
		return enabled;
	}

	@Override
	public Image getImage() {
		if (acquisition.isActive()) {
			return acquisition.getImage();
		} else {
			return null;
		}
	}

	@Override
	public IVideoDeviceControl getVideoControl() {
		return videoControl;
	}

	public WebCamProperties getWebCamProperties() {
		return RobotUtils.getRobotAttribute(WebCamProperties.class, servicesProvider);
	}

	@PostConstruct
	protected void initialize() {
		if (videoControl != null) {
			LOGGER.config("video control present: " + videoControl.getName());
		}

		LOGGER.config("initializing acquisition");
		acquisition = new Acquisition();
		try {
			if (enabled) {
				acquisition.open();
				acquisition.waitForInput();
				LOGGER.config("acquisition started successfully");
			}
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "unable to start acquisition", e);
		}
	}

	@Override
	public boolean isAvailable() {
		return acquisition.isActive();
	}

	@Override
	public void selectFormat(Format format) {
		acquisition.selectFormat(format);
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public void setServicesProvider(IRobotServiceProvider provider) {
		servicesProvider = provider;
		provider.registerService(IVisionService.class, this);
	}

	public void setVideoControl(IVideoDeviceControl videoControl) {
		this.videoControl = videoControl;
	}

	@PreDestroy
	protected void shutdown() {
		acquisition.close();
	}
}
