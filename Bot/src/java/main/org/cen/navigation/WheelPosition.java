package org.cen.navigation;

/**
 * Encapsulation of the position of left and right encoder (neg / pos).
 */
public class WheelPosition {

    private final long left;

    private final long right;

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
