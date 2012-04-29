package org.cen.robot.match.strategy.impl;

public class SimpleTarget extends AbstractTarget {
	private double gain;

	private boolean available;

	public SimpleTarget(String name, double gain) {
		super(name);
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
