package org.cen.cup.cup2012.device.arm2012.com;

import org.cen.com.out.OutData;
import org.cen.com.utils.ComDataUtils;
import org.cen.cup.cup2012.device.arm2012.ArmType2012;

public abstract class Arm2012OutData extends OutData {

	private final ArmType2012 type;

	public Arm2012OutData(ArmType2012 type) {
		this.type = type;
	}

	public ArmType2012 getType() {
		return type;
	}

	@Override
	public String getArguments() {
		String result = ComDataUtils.format(type.getArmIndex(), 2);
		return result;
	}

	@Override
	public String getDebugString() {
		return "[armIndex=" + type.getArmIndex() + "]";
	}
}
