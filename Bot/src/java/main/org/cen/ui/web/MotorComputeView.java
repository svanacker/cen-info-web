package org.cen.ui.web;

import java.math.BigDecimal;

import org.cen.robot.attributes.IRobotDimension;
import org.cen.robot.services.IRobotServiceProvider;
import org.cen.robot.utils.RobotUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * Store information to be able to compute some information about motor.
 */
public class MotorComputeView {
	/** The number of rotation for the motor by minute. */
	protected double motorRotationByMinute;

	/** The torque of the motors in N meter. */
	protected double motorTorque;

	/** The factor of reduction of the reductor. */
	protected int reductionFactor;

	/** The efficiency of the reductor (between 0 and 1). */
	protected double efficiency;

	/** the nominal counter of encoder. */
	protected int encoderCounter;

	/** The diameter of the wheel in mm. */
	protected double wheelDiameter;

	/** The weight of the robot in kg. */
	protected double robotWeight;

	private IRobotServiceProvider servicesProvider;

	/**
	 * Get the distance for a robot when reaching the maximal Speed. V =
	 * acceleration * time D = acceleration * time * time / 2
	 */
	public double getDistanceWhenReachMaxSpeed() {
		return round(getRobotMaxAcceleration() * getTimeToReachMaxSpeed() * getTimeToReachMaxSpeed() / 2.0d);
	}

	public double getEfficiency() {
		return efficiency;
	}

	// Read-only Compute result

	public int getEncoderCounter() {
		return encoderCounter;
	}

	public double getForceAtTheWheel() {
		if (wheelDiameter > 0) {
			return round(getReductorTorque() / wheelDiameter);
		}
		return -1;
	}

	public double getMotorRotationByMinute() {
		return motorRotationByMinute;
	}

	public double getMotorTorque() {
		return motorTorque;
	}

	public int getReductionFactor() {
		return reductionFactor;
	}

	public double getReductorTorque() {
		return round(motorTorque * reductionFactor * efficiency);
	}

	/**
	 * Maximal acceleration for a robot (m s-�). - Sum Force = weight *
	 * acceleration - Sum Force = 2 * ForceAtTheWheel (2 wheels) => Acceleration
	 * = 2 * ForceAtTheWheel / robotWeight
	 */
	public double getRobotMaxAcceleration() {
		if (robotWeight > 0) {
			return round(2.0d * getForceAtTheWheel() / robotWeight);
		} else {
			return -1;
		}
	}

	/**
	 * Speed in m / seconde.
	 */
	public double getRobotMaxSpeed() {
		return round(getRotationBySecondAtReductor() * wheelDiameter * Math.PI);
	}

	public double getRobotWeight() {
		return robotWeight;
	}

	public double getRotationByMinuteAtReductor() {
		if (reductionFactor > 0) {
			return round(motorRotationByMinute / reductionFactor);
		} else {
			return -1;
		}
	}

	public double getRotationBySecondAtReductor() {
		return round(getRotationByMinuteAtReductor() / 60.0d);
	}

	public double getTimeFor40degreeAtReductor() {
		return round(getTimeForOneRotationAtReductor() * (40.0d / 360.0d));
	}

	// Getters and setters

	public double getTimeForOneRotationAtReductor() {
		return round(1 / getRotationBySecondAtReductor());
	}

	/**
	 * Get the time for a robot to reach the maximal speed. Time = Max Speed (m
	 * / s) / Acceleration (m / s-2)
	 * 
	 * @return
	 */
	public double getTimeToReachMaxSpeed() {
		return round(getRobotMaxSpeed() / getRobotMaxAcceleration());
	}

	/**
	 * Get the time for a robot to go to one meter.
	 * 
	 * @return
	 */
	public double getTimeToReachOneMeter() {
		if (getRobotMaxSpeed() > 0) {
			return round((2.0d - 2.0d * getDistanceWhenReachMaxSpeed()) / getRobotMaxSpeed() + 2.0d * getTimeToReachMaxSpeed());
		}
		return -1.0d;
	}

	/**
	 * Speed = acceleration * time => time = Speed / acceleration
	 * 
	 * @return
	 */
	public double getTimeToReachOneMeterBySecond() {
		if (getRobotMaxAcceleration() > 0) {
			return round(1.0d / getRobotMaxAcceleration());
		}
		return -1d;
	}

	public double getWheelDiameter() {
		return wheelDiameter;
	}

	protected double round(double value) {
		return BigDecimal.valueOf(value).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public void setEfficiency(double efficiency) {
		this.efficiency = efficiency;
	}

	public void setEncoderCounter(int encoderCounter) {
		this.encoderCounter = encoderCounter;
	}

	public void setMotorRotationByMinute(double motorRotationByMinute) {
		this.motorRotationByMinute = motorRotationByMinute;
	}

	public void setMotorTorque(double motorTorque) {
		this.motorTorque = motorTorque;
	}

	public void setReductionFactor(int reductionFactor) {
		this.reductionFactor = reductionFactor;
	}

	public void setRobotWeight(double robotWeight) {
		this.robotWeight = robotWeight;
	}

	@Required
	public void setServicesProvider(IRobotServiceProvider provider) {
		this.servicesProvider = provider;
		initialize();
	}

	private void initialize() {
		IRobotDimension dimension = RobotUtils.getRobotAttribute(IRobotDimension.class, servicesProvider);
		motorRotationByMinute = dimension.getLeftMotor().getRotationsPerSecond() * 60;
		motorTorque = dimension.getLeftMotor().getMotorTorque();
		wheelDiameter = dimension.getLeftMotor().getWheelDiameter() / 1000;
		robotWeight = dimension.getWeight();
		encoderCounter = dimension.getLeftMotor().getPulseCount();
		reductionFactor = 14;
		efficiency = 0.8;
	}

	public void setWheelDiameter(double wheelDiameter) {
		this.wheelDiameter = wheelDiameter;
	}
}
