package org.cen.cup.cup2008.device.container;

public final class GetObjectCountResult extends ContainerResult {
	private int count;

	public GetObjectCountResult(ContainerRequest request, int count) {
		super(request);
		this.count = count;
	}

	public int getCount() {
		return count;
	}
}
