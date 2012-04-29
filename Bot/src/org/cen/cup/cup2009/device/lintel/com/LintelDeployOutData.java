package org.cen.cup.cup2009.device.lintel.com;

/**
 * The outData instruction to open the deploy the plier for lintel.
 */
public class LintelDeployOutData extends LintelOutData {
	
	public LintelDeployOutData() {
		super(LintelOutData.LINTEL_MOVE_SERVO_INDEX, 20, 2350);
	}

}
