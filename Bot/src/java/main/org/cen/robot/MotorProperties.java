package org.cen.robot;

import java.util.Properties;

import org.cen.util.PropertiesUtils;

public class MotorProperties {
	private static final String PROPERTY_PULSES_PER_ROTATION = "pulsesPerRotation";

	private static final String PROPERTY_ROTATIONS_PER_SECOND = "rotationsPerSecond";

	private static final String PROPERTY_TORQUE = "torque";

	private static final String PROPERTY_WHEEL_DIAMETER = "wheelDiameter";

	/**
	 * The torque of a motor in Newton meter.
	 */
	private double motorTorque;

	/**
	 * The number of pulse per rotation.
	 */
	private int pulseCount;

	/**
	 * The number of rotations per second of the motors in rotation / seconds.
	 */
	private double rotationsPerSecond;

	/**
	 * Diameter of the wheel in mm.
	 */
	private double wheelDiameter;

	public MotorProperties(double motorTorque, int pulseCount, double rotationsPerSecond, double wheelDiameter) {
		super();
		this.motorTorque = motorTorque;
		this.pulseCount = pulseCount;
		this.rotationsPerSecond = rotationsPerSecond;
		this.wheelDiameter = wheelDiameter;
	}

	public MotorProperties(Properties properties, String prefix) {
		super();
		setFromProperties(properties, prefix);
	}

	/**
	 * Converts an acceleration in mm / second² into pulses / second².
	 */
	public double accelerationToAccelerationPulse(double acceleration) {
		return acceleration * distanceToPulse(1.0d);
	}

	/**
	 * Converts a distance into pulses.
	 * 
	 * @param distance
	 *            a distance in mm
	 * @return the number of pulses
	 */
	public double distanceToPulse(double distance) {
		return distance * (pulseCount / getWheelPerimeter());
	}

	/**
	 * Gets the maximum speed in mm / second. The rotation is multiplied by the
	 * perimeter of the wheel.
	 */
	public double getMaxSpeed() {
		return rotationsPerSecond * getWheelPerimeter();
	}

	public double getMotorTorque() {
		return motorTorque;
	}

	public int getPulseCount() {
		return pulseCount;
	}

	public double getRotationsPerSecond() {
		return rotationsPerSecond;
	}

	public double getWheelDiameter() {
		return wheelDiameter;
	}

	/**
	 * Returns the perimeter of the wheel in mm.
	 */
	public double getWheelPerimeter() {
		return wheelDiameter * Math.PI;
	}

	/**
	 * Converts a distance into pulse.
	 * 
	 * @param pulse
	 *            the number of pulses to convert
	 * @return the computed distance
	 */
	public double pulseToDistance(double pulse) {
		return pulse * (getWheelPerimeter() / pulseCount);
	}

	public void setFromProperties(Properties properties, String prefix) {
		motorTorque = PropertiesUtils.getDouble(properties, prefix + PROPERTY_TORQUE);
		pulseCount = (int) PropertiesUtils.getDouble(properties, prefix + PROPERTY_PULSES_PER_ROTATION);
		rotationsPerSecond = PropertiesUtils.getDouble(properties, prefix + PROPERTY_ROTATIONS_PER_SECOND);
		wheelDiameter = PropertiesUtils.getDouble(properties, prefix + PROPERTY_WHEEL_DIAMETER);
	}

	/**
	 * Converts a speed from mm / second into pulse / second.
	 */
	public double speedToSpeedPulse(double speed) {
		return speed * distanceToPulse(1d);
	}

	@Override
	public String toString() {
		return getClass().getName() + "[torque=" + motorTorque + ", pulsesPerRotation=" + pulseCount + ", rotationsPerSecond=" + rotationsPerSecond + ", wheelDiameter=" + wheelDiameter + "]";
	}
}
