package org.cen.ui.web;

import java.awt.Graphics2D;
import java.awt.Image;

import org.cen.robot.IRobotService;
import org.cen.robot.IRobotServiceProvider;
import org.cen.vision.IVisionService;

public class VideoPreview implements IRobotService {
	private int refreshInterval = 2000;

	private IRobotServiceProvider servicesProvider;

	private Image getImage() {
		IVisionService vision = getVisionService();
		if (vision == null) {
			return null;
		}
		return vision.getImage();
	}

	public int getRefreshInterval() {
		return refreshInterval;
	}

	public long getTimeStamp() {
		return System.currentTimeMillis();
	}

	protected IVisionService getVisionService() {
		return servicesProvider.getService(IVisionService.class);
	}

	public boolean isAvailable() {
		return getVisionService().isAvailable();
	}

	public void paintFilter(Graphics2D g, Object data) {

	}

	public void paintInput(Graphics2D g, Object data) {
		Image image = getImage();
		if (image == null) {
			return;
		}
		g.setClip(0, 0, image.getWidth(null), image.getHeight(null));
		g.drawImage(image, 0, 0, null);
	}

	public void setRefreshInterval(int refreshInterval) {
		this.refreshInterval = refreshInterval;
	}

	@Override
	public void setServicesProvider(IRobotServiceProvider provider) {
		servicesProvider = provider;
	}
}
