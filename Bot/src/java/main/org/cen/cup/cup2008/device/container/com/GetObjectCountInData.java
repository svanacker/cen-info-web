package org.cen.cup.cup2008.device.container.com;

import org.cen.com.in.InData;

public class GetObjectCountInData extends InData {
	public static final String HEADER = "t";

	private int count;

	public GetObjectCountInData(int count) {
		super();
		this.count = count;
	}

	public int getCount() {
		return count;
	}
}
