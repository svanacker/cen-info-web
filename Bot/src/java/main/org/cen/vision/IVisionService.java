package org.cen.vision;

import java.awt.Image;

import javax.media.Format;

import org.cen.robot.services.IRobotService;
import org.cen.vision.control.IVideoDeviceControl;
import org.cen.vision.dataobjects.CalibrationDescriptor;
import org.cen.vision.dataobjects.WebCamProperties;

/**
 * Defines the contract of the Vision service for the robot.
 */
public interface IVisionService extends IRobotService {
	/**
	 * Returns all the formats supported by the video device.
	 * 
	 * @return an array of all the formats supported by the video device
	 */
	public Format[] getAvailableFormats();

	/**
	 * Returns the calibration descriptor.
	 * 
	 * @return the calibration descriptor
	 */
	public CalibrationDescriptor getCalibrationDescriptor();

	/**
	 * Returns the device enabled state
	 * 
	 * @return true if the device is enabled, false otherwise
	 */
	public boolean getEnabled();

	/**
	 * Acquires and return the acquired image.
	 * 
	 * @return the image acquired by the vision device
	 */
	public Image getImage();

	/**
	 * Returns the controller of the video device.
	 * 
	 * @return the video device controller
	 */
	public IVideoDeviceControl getVideoControl();

	/**
	 * Returns the properties of the web cam.
	 * 
	 * @return the properties of the web cam
	 */
	public WebCamProperties getWebCamProperties();

	/**
	 * Returns true if the service is available.
	 * 
	 * @return true if the service is available
	 */
	public boolean isAvailable();

	/**
	 * Selects the specified video format for image acquisition.
	 * 
	 * @param format
	 *            the video format to use for acquisition
	 */
	public void selectFormat(Format format);

	/**
	 * Enables or disables the device
	 * 
	 * @param enabled
	 *            the device state
	 */
	public void setEnabled(boolean enabled);
}
