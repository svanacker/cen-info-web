package org.cen.util;

import java.awt.Dimension;
import java.util.Properties;

import javax.vecmath.Vector3d;

import org.cen.math.Size2D;

/**
 * Helper class for reading properties.
 * 
 * @author Emmanuel ZURMELY
 */
public class PropertiesUtils {

	public static Dimension getDimension(Properties properties, String key) {
		Size2D size = getSize(properties, key);
		return new Dimension((int) size.getWidth(), (int) size.getHeight());
	}

	public static double getDouble(Properties properties, String key) {
		return getDouble(properties, key, Double.NaN);
	}

	/**
	 * Returns a double value read from the specified properties.
	 * 
	 * @param properties
	 *            the properties
	 * @param key
	 *            the key
	 * @param defaultValue
	 *            the default value to return if the key is not found or if the
	 *            value is incorrect
	 * @return the double value associated to the key if it exists, the default
	 *         value otherwise
	 */
	public static double getDouble(Properties properties, String key, double defaultValue) {
		String value = properties.getProperty(key);
		if (value == null) {
			System.err.println("missing property: " + key);
		}
		try {
			return Double.valueOf(value);
		} catch (Exception e) {
			System.err.println("invalid property value: " + key + "=" + value);
			e.printStackTrace();
		}
		return defaultValue;
	}

	/**
	 * Reads a dimension from the properties. The following syntaxes are
	 * supported:
	 * <ul>
	 * <li>[key]=width,height
	 * <li>key_width=width and key_height=height
	 * </ul>
	 * 
	 * @param properties
	 *            the properties
	 * @param key
	 *            the key
	 * @return the dimension object read from the properties
	 */
	public static Size2D getSize(Properties properties, String key) {
		String s = properties.getProperty(key);
		if (s.isEmpty()) {
			s = properties.getProperty(key + "_width") + "," + properties.getProperty(key + "_height");
		}
		String[] c = s.split(",");
		if (c.length != 2) {
			return null;
		}
		double[] d = new double[2];
		for (int i = 0; i < 2; i++) {
			d[i] = Double.valueOf(c[i]);
		}
		return new Size2D(d[0], d[1]);
	}

	/**
	 * Reads a three-dimensional vector from the properties. The following
	 * syntaxes are supported:
	 * <ul>
	 * <li>[key]=x,y,z
	 * <li>key_x=x and key_y=y and key_z=z
	 * </ul>
	 * 
	 * @param properties
	 *            the properties
	 * @param key
	 *            the key
	 * @return the vector object read from the properties
	 */
	public static Vector3d getVector(Properties properties, String key) {
		String s = properties.getProperty(key);
		if (s.isEmpty()) {
			s = properties.getProperty(key + "_x") + "," + properties.getProperty(key + "_y") + ","
					+ properties.getProperty(key + "_z");
		}
		String[] c = s.split(",");
		if (c.length != 3) {
			return null;
		}
		double[] d = new double[3];
		for (int i = 0; i < 3; i++) {
			d[i] = Double.valueOf(c[i]);
		}
		return new Vector3d(d);
	}
}
