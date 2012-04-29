package org.cen.robot.device.collision.com;

import org.cen.com.out.OutData;

public class CollisionReadOutData extends OutData {

	private static final String HEADER = "L";

	@Override
	public String getHeader() {
		return HEADER;
	}
}
