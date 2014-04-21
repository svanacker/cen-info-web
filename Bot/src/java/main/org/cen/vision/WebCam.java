package org.cen.vision;

import org.cen.vision.dataobjects.CalibrationDescriptor;
import org.cen.vision.dataobjects.WebCamProperties;

/**
 * The Webcam objects encapsulated a webcam and provides some useful methods
 * 
 * @author svanacker
 * @version 13/03/2007
 */
@Deprecated
public class WebCam {
	/**
	 * Properties of the WebCam
	 */
	protected WebCamProperties properties;

	/**
	 * The descriptor of the calibration with the data
	 */
	protected CalibrationDescriptor calibrationDescriptor;

	/**
	 * 
	 * @param properties
	 *            properties of the WebCam
	 */
	public WebCam(WebCamProperties properties, CalibrationDescriptor descriptor) {
		super();
		this.properties = properties;
		this.calibrationDescriptor = descriptor;
	}

	public WebCamProperties getProperties() {
		return properties;
	}

	public void setProperties(WebCamProperties properties) {
		this.properties = properties;
	}

	public CalibrationDescriptor getCalibrationDescriptor() {
		return calibrationDescriptor;
	}

	public void setDescriptor(CalibrationDescriptor descriptor) {
		this.calibrationDescriptor = descriptor;
	}
}
