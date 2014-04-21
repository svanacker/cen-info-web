package org.cen.cup.cup2012.device.arm2012;

public enum ArmType2012 {

	LEFT(0x01),

	RIGHT(0x02);

	private int armIndex;

	private ArmType2012(int armIndex) {
		this.armIndex = armIndex;
	}

	public int getArmIndex() {
		return armIndex;
	}
}
