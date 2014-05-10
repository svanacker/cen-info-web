package org.cen.ui.web;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.renderable.ParameterBlock;

import javax.media.Format;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;

import org.cen.robot.services.IRobotService;
import org.cen.robot.services.IRobotServiceProvider;
import org.cen.vision.IVisionService;

@Deprecated
public class VisionView implements IRobotService {
	private final float[] f = new float[320 * 240 * 3];

	private IRobotServiceProvider servicesProvider;

	private int value = 0;

	private int videoFormat = 0;

	public FormatItem[] getAvailableFormats() {
		Format[] formats = getVisionService().getAvailableFormats();
		FormatItem[] items = new FormatItem[formats.length];
		for (int i = 0; i < formats.length; i++) {
			items[i] = new FormatItem(formats[i].toString(), i);
		}
		return items;
	}

	private Image getImage() {
		return getVisionService().getImage();
	}

	public long getTimeStamp() {
		return System.currentTimeMillis();
	}

	public int getValue() {
		return value;
	}

	public int getVideoFormat() {
		return videoFormat;
	}

	protected IVisionService getVisionService() {
		return servicesProvider.getService(IVisionService.class);
	}

	public boolean isAvailable() {
		IVisionService service = getVisionService();
		if (service == null) {
			return false;
		}
		return service.isAvailable();
	}

	public void paint(Graphics2D g2d, Object obj) {
		Image image = getImage();
		g2d.drawImage(image, 0, 0, null);
	}

	public void paintFilter(Graphics2D g2d, Object obj) {
		Image image = getImage();

		ParameterBlock pb = new ParameterBlock();
		pb.addSource(image);
		pb.add(value);
		pb.add(f);
		PlanarImage target = JAI.create("ColorFilter", pb, null);
		Image out = target.getAsBufferedImage();

		g2d.drawImage(out, 0, 0, null);
	}

	@Override
	public void setServicesProvider(IRobotServiceProvider provider) {
		servicesProvider = provider;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void setVideoFormat(int videoFormat) {
		if (this.videoFormat == videoFormat)
			return;

		this.videoFormat = videoFormat;
		IVisionService vision = getVisionService();
		Format[] formats = vision.getAvailableFormats();
		vision.selectFormat(formats[videoFormat]);
	}
}
