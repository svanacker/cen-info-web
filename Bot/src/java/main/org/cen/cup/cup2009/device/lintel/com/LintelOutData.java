package org.cen.cup.cup2009.device.lintel.com;

import org.cen.robot.device.servo.com.ServoOutData;

/**
 * The abstract Lintel Interface
 */
public abstract class LintelOutData extends ServoOutData {
	
	/** The plier to open / close the plier for lintel . */
	public static final int LINTEL_PLIER_SERVO_INDEX = 18;
	
	/** The plier to deploy / undeploy the plier. */
	public static final int LINTEL_MOVE_SERVO_INDEX = 19;

	public LintelOutData(int id, int speed, int value) {
		super(id, speed, value);
	}
}
