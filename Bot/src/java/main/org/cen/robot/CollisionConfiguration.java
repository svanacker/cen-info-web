package org.cen.robot;

import java.util.Properties;

import org.cen.math.PropertiesMathUtils;

public class CollisionConfiguration implements IRobotAttribute {
	
	private static final String PROPERTY_STARTANGLE = "startAngle";

	private static final String PROPERTY_EXTENT = "extent";

	private static final String PROPERTY_DISTANCE = "distance";

	private static final String PROPERTY_OFFSET = "offset";

	private double startAngle;

	private double extent;

	private double distance;

	private double offset;

	public CollisionConfiguration(Properties properties, String prefix) {
		super();
		setFromProperties(properties, prefix);
	}

	/**
	 * The maximum collision distance from the bounds of the robot in mm.
	 * 
	 * @return the maximum collision distance
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * The extent angle in degrees.
	 * 
	 * @return the extent angle in degrees
	 */
	public double getExtent() {
		return extent;
	}

	/**
	 * The y offset of the collision area in mm
	 * 
	 * @return the y offset of the collision area
	 */
	public double getOffset() {
		return offset;
	}

	/**
	 * The starting angle in degrees.
	 * 
	 * @return the starting angle in degrees
	 */
	public double getStartAngle() {
		return startAngle;
	}

	public void setFromProperties(Properties properties, String prefix) {
		startAngle = PropertiesMathUtils.getDouble(properties, prefix + PROPERTY_STARTANGLE);
		extent = PropertiesMathUtils.getDouble(properties, prefix + PROPERTY_EXTENT);
		distance = PropertiesMathUtils.getDouble(properties, prefix + PROPERTY_DISTANCE);
		offset = PropertiesMathUtils.getDouble(properties, prefix + PROPERTY_OFFSET);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[startAngle=" + startAngle + "°, extent=" + extent + "°, distance=" + distance + " mm, offset=" + offset + " mm]";
	}
}
