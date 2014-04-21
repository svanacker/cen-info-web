package org.cen.cup.cup2009.device.plier.com;

import org.cen.robot.device.servo.com.ServoOutData;

/**
 * Abstract class to move the plier.
 */
public abstract class PlierOutData extends ServoOutData {
	
	public static final int PLIER_MOVE_SERVO_INDEX = 21;
	
	public static final int PLIER_SERVO_INDEX = 16;
	
	public static final int SPEED_HIGH = 255;
	
	public static final int SPEED_NORMAL = 100;
	
	public static final int SPEED_SLOW = 25;

	public PlierOutData(int id, int speed, int value) {
		super(id, speed, value);
	}
}
