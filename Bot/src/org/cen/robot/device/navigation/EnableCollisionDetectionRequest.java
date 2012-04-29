package org.cen.robot.device.navigation;

public class EnableCollisionDetectionRequest extends NavigationRequest {
	private final boolean enabled;

	public EnableCollisionDetectionRequest(boolean enabled) {
		super();
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[enabled=" + enabled + "]";
	}
}
