package org.cen.vision.control;

/**
 * Interface of the video device controllers.
 * 
 * @author Emmanuel ZURMELY
 * 
 */
public interface IVideoDeviceControl {
	/**
	 * Returns the name of this controller.
	 * 
	 * @return the name of this controller
	 */
	public String getName();

	/**
	 * Query the video device and returns the value of the specified property.
	 * The value is normalized to fit the range [0, 1].
	 * 
	 * @param property
	 *            the video device property to read
	 * @return the value of the property
	 */
	public double getProperty(VideoDeviceProperty property);

	/**
	 * Returns true if the video device is present, initialized and ready to be
	 * controlled by this controller.
	 * 
	 * @return true if the video controller is available, false otherwise
	 */
	public boolean isAvailable();

	/**
	 * Sets the value of the specified property. The value must be in the range
	 * [0, 1].
	 * 
	 * @param property
	 *            the video device property to set
	 * @param value
	 *            the new value to set to the property
	 */
	public void setProperty(VideoDeviceProperty property, double value);
}
