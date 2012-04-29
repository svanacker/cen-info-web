package org.cen.simulRobot.device.collision.com;

import org.cen.com.in.InData;

public class CollisionSimulReadInData extends InData {
	public static final String HEADER = "g";

	/**
	 * The acceleration that must be used The value is given in : pulse / (s /
	 * refreshRate)
	 */
	protected int acceleration;

	/**
	 * The instruction for the left Motor which is given as a distance in
	 * "increment" of the coder incremental The value is between -32768 and
	 * +32767
	 */
	protected int left;

	/**
	 * The instruction for the right Motor which is given as a distance in
	 * "increment" of the coder incremental The value is between -32768 and
	 * +32767
	 */
	protected int right;

	/**
	 * The maximal speed that the wheels must reach : The value is given in :
	 * pulse / (s / refreshRate)
	 */
	protected int speed;

	/**
	 * Constructor with all arguments.
	 * 
	 * @param left
	 *            the left Position which must be reached.
	 * @param right
	 *            the right Position which must be reached
	 * @param speed
	 *            the maximal speed which must be used
	 * @param acceleration
	 *            the maximal acceleration which must be used to reach the speed
	 */

	public CollisionSimulReadInData(int left, int right, int speed, int acceleration) {
		super();
		this.left = left;
		this.right = right;
		this.speed = speed;
		this.acceleration = acceleration;
	}


	//	@Override
	//	public String getHeader() {
	//		return HEADER;
	//	}

	public int getAcceleration() {
		return acceleration;
	}

	public int getLeft() {
		return left;
	}

	public int getRight() {
		return right;
	}

	public int getSpeed() {
		return speed;
	}


	@Override
	public String toString() {
		return getClass().getSimpleName() + "[left=" + left + ", right=" + right + ", speed=" + speed + ", acceleration=" + acceleration + "]";
	}
}
