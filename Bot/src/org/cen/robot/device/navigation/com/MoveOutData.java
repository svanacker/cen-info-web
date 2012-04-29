package org.cen.robot.device.navigation.com;

import org.cen.com.ComDataUtils;
import org.cen.com.documentation.DeviceDataSignature;
import org.cen.com.documentation.DeviceMethodSignature;
import org.cen.com.documentation.DeviceMethodType;
import org.cen.com.documentation.DeviceParameter;
import org.cen.com.documentation.DeviceParameterType;
import org.cen.com.out.OutData;
import org.cen.robot.device.navigation.NavigationDevice;

/**
 * Corresponds to the data which is sent to the javelin to move the robot
 * 
 * @author svanacker
 * @version 07/03/2007
 */
@DeviceDataSignature(deviceName = NavigationDevice.NAME, methods = { @DeviceMethodSignature(
		header = MoveOutData.HEADER,
		type = DeviceMethodType.INPUT,
		parameters = { @DeviceParameter(name = "left", length = 4, type = DeviceParameterType.SIGNED, unit = "mm"),
				@DeviceParameter(name = "right", length = 4, type = DeviceParameterType.SIGNED, unit = "mm"),
				@DeviceParameter(name = "speed", length = 2, type = DeviceParameterType.SIGNED, unit = ""),
				@DeviceParameter(name = "acceleration", length = 2, type = DeviceParameterType.SIGNED, unit = "") }) })
public class MoveOutData extends OutData {

	static final String HEADER = "g";

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
	public MoveOutData(int left, int right, int speed, int acceleration) {
		this.left = left;
		this.right = right;
		this.speed = speed;
		this.acceleration = acceleration;
	}

	public double getAcceleration() {
		return acceleration;
	}

	@Override
	public String getArguments() {
		String leftString = ComDataUtils.format(left, 4);
		String rightString = ComDataUtils.format(right, 4);
		String accelerationString = ComDataUtils.format(acceleration, 2);
		String speedString = ComDataUtils.format(speed, 2);

		return leftString + rightString + accelerationString + speedString;
	}

	@Override
	public String getDebugString() {
		return "[l=" + left + ", r=" + right + ", s=" + speed + ", a=" + acceleration + "]";
	}

	@Override
	public String getHeader() {
		return HEADER;
	}

	public int getLeft() {
		return left;
	}

	public int getRight() {
		return right;
	}

	public double getSpeed() {
		return speed;
	}
}
