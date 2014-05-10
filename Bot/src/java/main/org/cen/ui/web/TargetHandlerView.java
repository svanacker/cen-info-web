package org.cen.ui.web;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import org.cen.robot.services.IRobotService;
import org.cen.robot.services.IRobotServiceProvider;
import org.cen.vision.filters.TargetStat;
import org.cen.vision.filters.TargetStat.TargetLocation;
import org.cen.vision.util.GoalTargetHandler;
import org.cen.vision.util.ITargetHandler;
import org.cen.vision.util.TargetDebugListener;
import org.cen.vision.util.TargetHandler;
import org.cen.vision.util.TargetHandlerThread;

public class TargetHandlerView implements IRobotService, TargetDebugListener {
	private Image acquired;

	private int bottomBorder;

	private int colorAngle;

	private double[][] history;

	private int intensityThreshold;

	private TargetLocation[] locations;

	private int newDataRatio;

	private int oldDataRatio;

	private int saturationThreshold;

	private IRobotServiceProvider serviceProvider;

	private int slope = 40;

	private int topBorder;

	private int weightThreshold = 150;

	private void change() {
		TargetHandler targetHandler = getTargetHandler();
		targetHandler.setFilteredColor(colorAngle / 100d);
		targetHandler.setIntensityThreshold(intensityThreshold / 100d);
		targetHandler.setSaturationThreshold(saturationThreshold / 100d);
		if (targetHandler instanceof GoalTargetHandler) {
			GoalTargetHandler goalTargetHandler = (GoalTargetHandler) targetHandler;
			goalTargetHandler.setBottomBound(bottomBorder / 100d);
			goalTargetHandler.setTopBound(topBorder / 100d);
			goalTargetHandler.setOldDataRatio(oldDataRatio / 100d);
			goalTargetHandler.setNewDataRatio(newDataRatio / 100d);
			goalTargetHandler.setSlope(slope / 10d);
		}
	}

	public void clearHistory() {
		getTargetHandler().clearHistory();
	}

	@Override
	public void debug(TargetHandler targetHandler, Image img, TargetStat stats, double[][] historyGrid) {
		// ParameterBlock pb = new ParameterBlock();
		// pb.addSource(img);
		// pb.add(0.01 * angleSlider.getValue());
		//
		// if (colorFilterCheckBox.isSelected()) {
		// pb = new ParameterBlock();
		// pb.addSource(img);
		// pb.add(angleSlider.getValue());
		// pb.add(f);
		// PlanarImage target = JAI.create("ColorFilter", pb, null);
		// Image im = target.getAsBufferedImage();
		// if (historyCheckBox.isSelected()) {
		// drawHistory(im, history);
		// }
		// outImage.setIcon(new ImageIcon(im));
		// }
		acquired = img;
		history = historyGrid;
		locations = ((GoalTargetHandler) targetHandler).getGoalBounds();
	}

	private void drawHistory(Graphics2D g, double[][] data) {
		Rectangle bounds = g.getClipBounds();
		double cx = bounds.getWidth() / TargetHandler.GRID_SIZE;
		double cy = bounds.getHeight() / TargetHandler.GRID_SIZE;
		int ly = (int) (data[0].length * topBorder / 100d);
		int hy = (int) (data[0].length * bottomBorder / 100d);
		g.setColor(Color.RED);
		g.fillRect(0, 0, (int) (cx * data.length), (int) (cy * ly));
		g.setColor(Color.GREEN);
		g.fillRect(0, (int) (cy * hy), (int) (cx * data.length), (int) (cy * data[0].length));
		for (int x = 0; x < data.length; x++)
			for (int y = ly; y < hy; y++) {
				float d = (float) data[x][y] / 2;
				Color c;
				if (d > (weightThreshold / 100d)) {
					c = new Color(d, 0, d, 1f);
				} else {
					c = new Color(d, d, d, 1f);
				}
				g.setColor(c);
				g.fillRect((int) (x * cx), (int) (y * cy), 10, 8);
			}
	}

	private void drawTarget(Graphics2D g, TargetLocation data) {
		if (data.getWeight() < weightThreshold / 100d) {
			g.drawString("No target", 0, 15);
			return;
		}
		Rectangle bounds = g.getClipBounds();
		double cx = bounds.getWidth() / TargetHandler.GRID_SIZE;
		double cy = bounds.getHeight() / TargetHandler.GRID_SIZE;
		Point p = data.getLocation();
		g.setColor(Color.red);
		g.fillOval((int) (p.x * cx), (int) (p.y * cy), 10, 10);
		g.setColor(Color.white);
		g.drawOval((int) (p.x * cx), (int) (p.y * cy), 10, 10);
	}

	public int getBottomBorder() {
		return bottomBorder;
	}

	public int getColorAngle() {
		return colorAngle;
	}

	public int getIntensityThreshold() {
		return intensityThreshold;
	}

	public int getNewDataRatio() {
		return newDataRatio;
	}

	public int getOldDataRatio() {
		return oldDataRatio;
	}

	public int getSaturationThreshold() {
		return saturationThreshold;
	}

	public int getSlope() {
		return slope;
	}

	private TargetHandler getTargetHandler() {
		ITargetHandler service = serviceProvider.getService(ITargetHandler.class);
		return service.getTargetHandler();
	}

	private TargetHandlerThread getTargetHandlerThread() {
		ITargetHandler service = serviceProvider.getService(ITargetHandler.class);
		return service.getTargetHandlerThread();
	}

	public long getTimeStamp() {
		return System.currentTimeMillis();
	}

	public int getTopBorder() {
		return topBorder;
	}

	public int getWeightThreshold() {
		return weightThreshold;
	}

	private void initializeListener() {
		GoalTargetHandler targetHandler = (GoalTargetHandler) getTargetHandler();
		bottomBorder = (int) (targetHandler.getBottomBound() * 100);
		topBorder = (int) (targetHandler.getTopBound() * 100);
		colorAngle = (int) (targetHandler.getFilteredColor() * 100);
		saturationThreshold = (int) (targetHandler.getSaturationThreshold() * 100);
		intensityThreshold = (int) (targetHandler.getIntensityThreshold() * 100);
		weightThreshold = (int) (targetHandler.getWeightThreshold() * 100);
		oldDataRatio = (int) (targetHandler.getOldDataRatio() * 100);
		newDataRatio = (int) (targetHandler.getNewDataRatio() * 100);
		slope = (int) (targetHandler.getSlope() * 10);
		targetHandler.addDebugListener(this);
		TargetHandlerThread thread = getTargetHandlerThread();
		if (!thread.isRunning())
			thread.resume();
	}

	public void paintFilter(Graphics2D g, Object data) {
		if (acquired == null)
			return;

		g.setClip(0, 0, acquired.getWidth(null), acquired.getHeight(null));

		drawHistory(g, history);
	}

	public void paintInput(Graphics2D g, Object data) {
		if (acquired == null)
			return;
		g.setClip(0, 0, acquired.getWidth(null), acquired.getHeight(null));
		g.drawImage(acquired, 0, 0, null);

		if (locations == null)
			return;
		drawTarget(g, locations[0]);
		drawTarget(g, locations[1]);
	}

	public void setBottomBorder(int bottomBorder) {
		if (this.bottomBorder == bottomBorder)
			return;

		this.bottomBorder = bottomBorder;
		change();
	}

	public void setColorAngle(int colorAngle) {
		if (this.colorAngle == colorAngle)
			return;

		this.colorAngle = colorAngle;
		change();
	}

	public void setIntensityThreshold(int intensityThreshold) {
		if (this.intensityThreshold == intensityThreshold)
			return;

		this.intensityThreshold = intensityThreshold;
		change();
	}

	public void setNewDataRatio(int newDataRatio) {
		if (this.newDataRatio == newDataRatio)
			return;

		this.newDataRatio = newDataRatio;
		change();
	}

	public void setOldDataRatio(int oldDataRatio) {
		if (this.oldDataRatio == oldDataRatio)
			return;

		this.oldDataRatio = oldDataRatio;
		change();
	}

	public void setSaturationThreshold(int saturationThreshold) {
		if (this.saturationThreshold == saturationThreshold)
			return;

		this.saturationThreshold = saturationThreshold;
		change();
	}

	@Override
	public void setServicesProvider(IRobotServiceProvider provider) {
		serviceProvider = provider;
		initializeListener();
	}

	public void setSlope(int slope) {
		if (this.slope == slope)
			return;

		this.slope = slope;
		change();
	}

	public void setTopBorder(int topBorder) {
		if (this.topBorder == topBorder)
			return;

		this.topBorder = topBorder;
		change();
	}

	public void setWeightThreshold(int weightThreshold) {
		if (this.weightThreshold == weightThreshold)
			return;

		this.weightThreshold = weightThreshold;
		change();
	}
}
