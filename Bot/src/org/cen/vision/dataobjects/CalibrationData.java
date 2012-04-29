package org.cen.vision.dataobjects;

/**
 * Object representing the data for the color calibration of the webcam.
 * 
 * @author Emmanuel ZURMELY
 */
public class CalibrationData {
	protected double error;

	protected double expected;

	protected String name;

	protected double result = Double.NaN;

	public CalibrationData(String name, double expected, double error) {
		super();
		this.name = name;
		this.error = error;
		this.expected = expected;
	}

	public void clear() {
		result = Double.NaN;
	}

	public double getError() {
		return error;
	}

	public double getExpected() {
		return expected;
	}

	public String getName() {
		return name;
	}

	public double getResult() {
		return result;
	}

	public double getValue() {
		if (Double.isNaN(result))
			return expected;
		else
			return result;
	}

	public void setError(double error) {
		this.error = error;
	}

	public void setExpected(double expected) {
		this.expected = expected;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setResult(double result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return getClass().getName() + "{name=" + name + ", result=" + result + ", expected=" + expected + ", error=" + error + "}";
	}
}
