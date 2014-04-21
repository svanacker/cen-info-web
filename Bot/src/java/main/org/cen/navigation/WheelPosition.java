package org.cen.navigation;

public class WheelPosition {
	private long left;

	private long right;

	public WheelPosition(long left, long right) {
		super();
		this.left = left;
		this.right = right;
	}

	public long getLeft() {
		return left;
	}

	public long getRight() {
		return right;
	}
}
