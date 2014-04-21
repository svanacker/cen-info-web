package org.cen.vision.coordinates;

import javax.vecmath.Vector3d;

import org.cen.vision.dataobjects.WebCamProperties;

/**
 * Factory of coordinates objects.
 * 
 * @author Emmanuel ZURMELY
 */
public class CoordinatesFactory {
	/**
	 * Returns a coordinates object initialized with the specified webcam
	 * properties.
	 * 
	 * @param properties
	 *            the properties of the webcam
	 * @return a coordinates system converter
	 */
	public static CoordinatesTransform getCoordinates(WebCamProperties properties) {
		Coordinates c = new Coordinates();
		Vector3d r = properties.getRotation();
		c.setInclination(r.x);
		c.setOrientation(r.z);
		c.setOrigin(properties.getPosition());
		c.setVisionAngles(properties.getVisionAngles());
		c.setImageDimension(properties.getImageDimension());
		return c;
	}
}
