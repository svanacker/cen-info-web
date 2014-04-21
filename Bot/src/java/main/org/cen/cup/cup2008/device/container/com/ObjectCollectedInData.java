package org.cen.cup.cup2008.device.container.com;

import org.cen.com.in.InData;

public final class ObjectCollectedInData extends InData {
	public static final String HEADER = "k";

	private int status;

	public ObjectCollectedInData(int status) {
		super();
		this.status = status;
	}

	public int getStatus() {
		return status;
	}
}
