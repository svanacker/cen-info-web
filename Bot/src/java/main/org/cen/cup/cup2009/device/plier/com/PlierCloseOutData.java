package org.cen.cup.cup2009.device.plier.com;

/**
 * The instruction to close the plier. 
 */
public class PlierCloseOutData extends PlierOutData {

	public PlierCloseOutData() {
		super(PlierOutData.PLIER_SERVO_INDEX, SPEED_HIGH, 650);
	}

}
