package org.cen.robot.device.collision.com;

import org.cen.com.out.OutData;
import org.cen.com.utils.ComDataUtils;

/**
 * Abstract class to enable or disable the collision system
 */
public abstract class CollisionDetectionOutData extends OutData {
	private static final String HEADER = "V";

	protected boolean value;

	protected CollisionDetectionOutData(boolean value) {
		this.value = value;
	}

	@Override
	public String getArguments() {
		String valueAsString = "";
		if (value) {
			valueAsString = ComDataUtils.format(1, 2);
		} else {
			valueAsString = ComDataUtils.format(0, 2);
		}

		return valueAsString;
	}

	@Override
	public String getHeader() {
		return HEADER;
	}

}
