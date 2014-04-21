package org.cen.cup.cup2008.device.container.com;

import org.cen.com.ComDataUtils;

public class MoveContainerOutData extends ContainerOutData {
	static final String HEADER = "J";

	@Override
	public String getHeader() {
		return HEADER;
	}
	private int data;

	public MoveContainerOutData(int data) {
		super();
		this.data = data;
	}

	@Override
	public String getArguments() {
		return ComDataUtils.format(data, 2);
	}
}
