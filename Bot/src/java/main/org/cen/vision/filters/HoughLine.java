package org.cen.vision.filters;

import org.cen.geom.Point2D;

public class HoughLine {
	private int height;

	private double[] parameters = new double[2];

	private double rho;

	private double theta;

	private int width;

	public HoughLine(double rho, double theta, int width, int height) {
		super();
		this.rho = rho;
		this.theta = theta;
		this.width = width;
		this.height = height;
		computeParameters();
	}

	private void computeParameters() {
		if (Math.abs(Math.sin(theta)) < 0.01) {
			// vertical
			parameters[0] = Double.NaN;
			parameters[1] = width / 2 + ((theta < (Math.PI / 2)) ? rho : -rho);
		} else {
			parameters[0] = -Math.cos(theta) / Math.sin(theta);
			parameters[1] = rho / Math.sin(theta) + height / 2 - parameters[0] * width / 2;
		}
	}

	public String getEquation() {
		if (Double.isNaN(parameters[0])) {
			return "y = " + parameters[1];
		} else {
			return "y = " + parameters[0] + " x + " + parameters[1];
		}
	}

	public Point2D getIntersection(HoughLine line) {
		double a1 = parameters[0];
		double b1 = parameters[1];
		double a2 = line.parameters[0];
		double b2 = line.parameters[1];
		double x = (b2 - b1) / (a1 - a2);
		double y = ((a1 + a2) * x + (b1 + b2)) / 2;
		return new Point2D.Double(x, y);
	}

	public double[] getParameters() {
		return parameters;
	}

	public double getRho() {
		return rho;
	}

	public double getTheta() {
		return theta;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[rho: " + rho + ", theta: " + theta + ", equation: " + getEquation() + "]";
	}
}
