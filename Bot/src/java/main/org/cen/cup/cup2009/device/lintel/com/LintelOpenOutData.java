package org.cen.cup.cup2009.device.lintel.com;

/**
 * The outData instruction to open the lintel plier.
 */
public class LintelOpenOutData extends LintelOutData {

	public LintelOpenOutData() {
		super(LintelOutData.LINTEL_PLIER_SERVO_INDEX, 255, 1300);
	}

}
