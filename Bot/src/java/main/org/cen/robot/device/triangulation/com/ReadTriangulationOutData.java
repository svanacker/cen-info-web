package org.cen.robot.device.triangulation.com;

import org.cen.com.out.OutData;

/**
 * Serial data object used for requesting the absolute position of the robot
 * from the triangulation device.
 * 
 * @author Emmanuel ZURMELY
 */
public class ReadTriangulationOutData extends OutData {
	static final String HEADER = "l";

	@Override
	public String getHeader() {
		return HEADER;
	}
}
