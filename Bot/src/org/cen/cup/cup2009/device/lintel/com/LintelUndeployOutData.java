package org.cen.cup.cup2009.device.lintel.com;

/**
 * The outData instruction to open the unDeploy the plier for lintel.
 */
public class LintelUndeployOutData extends LintelOutData {
	
	public LintelUndeployOutData() {
		super(LintelOutData.LINTEL_MOVE_SERVO_INDEX, 255, 600);
	}
}
