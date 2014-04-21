package org.cen.robot.match.strategy.impl;

import java.awt.geom.Point2D;

public class SimpleTarget extends AbstractTarget {
	private double gain;

	private boolean available;

	public SimpleTarget(String name, Point2D position, double gain) {
		super(name, position);
		this.gain = gain;
		available = true;
	}

	@Override
	public double getGain() {
		return gain;
	}

	@Override
	public boolean isAvailable() {
		return available;
	}

	@Override
	public void setAvailable(boolean available) {
		this.available = available;
	}

}
