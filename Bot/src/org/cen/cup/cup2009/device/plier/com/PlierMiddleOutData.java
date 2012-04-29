package org.cen.cup.cup2009.device.plier.com;

/**
 * The instrution to move the plier to the middle.
 */
public class PlierMiddleOutData extends PlierOutData {

	public PlierMiddleOutData() {
		super(PlierOutData.PLIER_MOVE_SERVO_INDEX, SPEED_HIGH, 1550);
	}

}
