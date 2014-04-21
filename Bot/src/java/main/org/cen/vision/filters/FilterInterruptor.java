package org.cen.vision.filters;

public class FilterInterruptor {
	private boolean interrupted = false;

	public boolean isInterrupted() {
		return interrupted;
	}

	public void setInterrupted(boolean interrupted) {
		this.interrupted = interrupted;
	}
}
