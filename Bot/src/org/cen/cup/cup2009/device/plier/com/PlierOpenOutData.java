package org.cen.cup.cup2009.device.plier.com;

/**
 * The instruction to open the plier.
 */
public class PlierOpenOutData extends PlierOutData {

	public PlierOpenOutData() {
		super(PlierOutData.PLIER_SERVO_INDEX, SPEED_SLOW,  1900);
	}

}
