package org.cen.vision.logitech;

public class Property {
	private int max;

	private int maxIndex;

	private int min;

	private int minIndex;

	private int valueIndex;

	public Property(int valueIndex) {
		this(valueIndex, -1, -1);
	}

	public Property(int valueIndex, int minIndex, int maxIndex) {
		super();
		this.valueIndex = valueIndex;
		this.minIndex = minIndex;
		this.maxIndex = maxIndex;
	}

	public int denormalize(double value) {
		return (int) (value * (max - min) + min);
	}

	public int getMaxIndex() {
		return maxIndex;
	}

	public int getMinIndex() {
		return minIndex;
	}

	public int getValueIndex() {
		return valueIndex;
	}

	public boolean hasValueRange() {
		return (minIndex != maxIndex);
	}

	public boolean isInitialized() {
		return (min != max);
	}

	public double normalize(int value) {
		return (double) (value - min) / (max - min);
	}

	void setRange(int min, int max) {
		this.min = min;
		this.max = max;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[min: " + min + ", max: " + max + ", valueIndex: " + valueIndex + ", minIndex: " + minIndex + ", maxIndex: " + maxIndex + "]";
	}
}
