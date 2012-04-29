package org.cen.cup.cup2009.device.plier.com;

/**
 * The instrution to move the plier to the left.
 */
public class PlierLeftOutData extends PlierOutData {

	public PlierLeftOutData() {
		super(PlierOutData.PLIER_MOVE_SERVO_INDEX, SPEED_HIGH, 2150);
	}

}
