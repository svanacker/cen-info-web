package org.cen.vision.dataobjects;

import java.awt.Dimension;
import java.util.Properties;

import javax.vecmath.Vector3d;

import org.cen.math.PropertiesMathUtils;
import org.cen.math.Size2D;
import org.cen.robot.AdvancedRobotAttribute;

/**
 * Encapsulated the properties of a webcam.
 * 
 * @author Emmanuel Zurmely
 * @version 11/03/2007
 */
public class WebCamProperties extends AdvancedRobotAttribute {
	private static final String KEY_IMAGEDIMENSION = "imageDimension";

	private static final String KEY_POSITION = "position";

	private static final String KEY_ROTATION = "rotation";

	private static final String KEY_VISIONANGLES = "visionAngles";

	/**
	 * Constructor.
	 */
	public WebCamProperties() {
		super();
	}

	/**
	 * Constructeur.
	 * 
	 * @param imageDimension
	 *            the dimension of the image in pixels
	 * @param visionAngles
	 *            angles of vision
	 * @param position
	 *            absolute position of the webcam
	 * @param rotation
	 *            the rotation angles of the webcam around the x, y and z axes.
	 */
	public WebCamProperties(Dimension imageDimension, Size2D visionAngles, Vector3d position, Vector3d rotation) {
		super();
		setImageDimension(imageDimension);
		setVisionAngles(visionAngles);
		setPosition(position);
		setRotation(rotation);
	}

	public Dimension getImageDimension() {
		return (Dimension) get(KEY_IMAGEDIMENSION);
	}

	/**
	 * Returns the absolute position of the webcam.
	 * 
	 * @return the absolute position of the webcam
	 */
	public Vector3d getPosition() {
		return (Vector3d) get(KEY_POSITION);
	}

	/**
	 * Returns the rotation angles of the webcam around the x, y and z axes.
	 * 
	 * @return the rotation angles of the webcam around the x, y and z axes
	 */
	public Vector3d getRotation() {
		return (Vector3d) get(KEY_ROTATION);
	}

	/**
	 * Returns the angles of vision.
	 * 
	 * @return the angles of vision
	 */
	public Size2D getVisionAngles() {
		return (Size2D) get(KEY_VISIONANGLES);
	}

	public void set(Properties properties) {
		setPosition(PropertiesMathUtils.getVector(properties, KEY_POSITION));
		setRotation(PropertiesMathUtils.getVector(properties, KEY_ROTATION));
		setImageDimension(PropertiesMathUtils.getDimension(properties, KEY_IMAGEDIMENSION));
		setVisionAngles(PropertiesMathUtils.getSize(properties, KEY_VISIONANGLES));
		String unit = properties.getProperty("angleUnit");
		if ((unit != null) && unit.equals("degrees")) {
			Size2D va = getVisionAngles();
			va.setSize(Math.toRadians(va.getWidth()), Math.toRadians(va.getHeight()));
			Vector3d r = getRotation();
			r.set(Math.toRadians(r.x), Math.toRadians(r.y), Math.toRadians(r.z));
		}
	}

	/**
	 * Sets the captured image dimension.
	 * 
	 * @param imageDimension
	 *            the dimension of the acquired images
	 */
	public void setImageDimension(Dimension imageDimension) {
		put(KEY_IMAGEDIMENSION, imageDimension);
	}

	/**
	 * Sets the absolute position of the webcam.
	 * 
	 * @param position
	 *            the absolute position of the webcam
	 */
	public void setPosition(Vector3d position) {
		put(KEY_POSITION, position);
	}

	/**
	 * Sets the rotation angles of the webcam around the x, y and z axes.
	 * 
	 * @param rotation
	 *            the rotation angles of the webcam around the x, y and z axes
	 */
	public void setRotation(Vector3d rotation) {
		put(KEY_ROTATION, rotation);
	}

	/**
	 * Sets the angles of vision.
	 * 
	 * @param visionAngle
	 *            the angles of vision
	 */
	public void setVisionAngles(Size2D visionAngles) {
		put(KEY_VISIONANGLES, visionAngles);
	}

	/**
	 * Définit l'inclinaison en fonction de la longueur du champ de vision.
	 * 
	 * @param distance
	 */
	public void setVisionLength(int distance) {
		double alpha = Math.atan2(distance, getPosition().z);
		alpha = Math.PI / 2d - alpha + getVisionAngles().getHeight() / 2d;
		setRotation(new Vector3d(alpha, 0, 0));
	}
}
