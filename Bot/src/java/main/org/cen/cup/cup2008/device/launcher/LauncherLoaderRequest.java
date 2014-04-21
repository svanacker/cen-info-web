package org.cen.cup.cup2008.device.launcher;

public class LauncherLoaderRequest extends LauncherRequest {
	public enum LoaderAction {
		LOCK, UNLOCK;
	}

	private LoaderAction action;

	public LauncherLoaderRequest(LoaderAction action) {
		super();
		this.action = action;
	}

	public LoaderAction getAction() {
		return action;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[action: " + action + "]";
	}
}
