package org.cen.ui.web;

import org.cen.robot.IRobotServiceProvider;
import org.cen.ui.swing.SwingUI;

public class SwingView {
	IRobotServiceProvider provider;

	SwingUI swingUI;

	private SwingUI getNewSwingUI() {
		return new SwingUI(provider);
	}

	public void setServicesProvider(IRobotServiceProvider provider) {
		this.provider = provider;
	}

	public void show() {
		if (swingUI == null) {
			swingUI = getNewSwingUI();
		}
		swingUI.toFront();
	}
}
