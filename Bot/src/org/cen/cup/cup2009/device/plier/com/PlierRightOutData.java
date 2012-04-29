package org.cen.cup.cup2009.device.plier.com;

/**
 * The instrution to move the plier to the right.
 */
public class PlierRightOutData extends PlierOutData {

	public PlierRightOutData() {
		super(PlierOutData.PLIER_MOVE_SERVO_INDEX, SPEED_HIGH, 700);
	}

}
