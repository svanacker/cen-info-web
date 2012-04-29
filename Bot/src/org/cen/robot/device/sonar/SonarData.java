package org.cen.robot.device.sonar;

import org.cen.ui.web.SonarView;

/**
 * Encapsulates the data for a sonar.
 */
public class SonarData {
	
	/** The id of the sonar. */
	private int id;

	/** The value (distance in mm). */
	private int value;

	private SonarView sonarView;

	public SonarData(int id, SonarView sonarView) {
		super();
		this.id = id;
		this.sonarView = sonarView;
	}

	private void changed() {
		sonarView.notifyChanged(this);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		if (value == this.value)
			return;
		this.value = value;
		changed();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[id: " + id + ", value: " + value
				+ "]";
	}
}
