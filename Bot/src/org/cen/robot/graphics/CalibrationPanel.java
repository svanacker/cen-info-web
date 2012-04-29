package org.cen.robot.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.color.ColorSpace;

import javax.media.jai.IHSColorSpace;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.cen.vision.filters.CalibrationStat;
import org.cen.vision.filters.TargetStat;
import org.cen.vision.filters.TargetStat.TargetLocation;
import org.cen.vision.util.CalibrationDebugListener;
import org.cen.vision.util.CalibrationHandler;
import org.cen.vision.util.TargetDebugListener;
import org.cen.vision.util.TargetHandler;

/**
 * Panel which gives some information about camera calibration
 * 
 * @author svanacker
 * @version 14/03/2007
 */
public class CalibrationPanel extends JPanel implements CalibrationDebugListener, TargetDebugListener {
	private static final long serialVersionUID = 1L;

	private CalibrationHandler calibrationHandler;

	public JLabel image;

	private TargetHandler targetHandler;

	public CalibrationPanel(CalibrationHandler calibrationHandler, TargetHandler targetHandler) {
		super();
		this.calibrationHandler = calibrationHandler;
		this.targetHandler = targetHandler;
		calibrationHandler.addDebugListener(this);
		if (targetHandler != null)
			targetHandler.addDebugListener(this);
		image = new JLabel();
		add(image);
	}

	public void debug(Image img, CalibrationStat stats) {
		drawCalibration(img, stats);
		image.setIcon(new ImageIcon(img));
	}

	public void debug(TargetHandler targetHandler, Image img, TargetStat stats, double[][] historyGrid) {
		TargetLocation target = targetHandler.getBestTarget(0);
		drawTarget(img, target);
		image.setIcon(new ImageIcon(img));
	}

	private void drawCalibration(Image img, CalibrationStat cstats) {
		Graphics2D g = (Graphics2D) img.getGraphics();
		CalibrationStat.Sample[] s = cstats.getSamples();
		ColorSpace c = IHSColorSpace.getInstance();
		int w = img.getWidth(null);
		float[] f = new float[3];
		int n = s.length;
		for (int i = n - 1; i >= 0; i--) {
			// System.out.println(s[i]);
			if (s[i].getDistance() < calibrationHandler.getDistanceThreshold()) {
				f[0] = 1f;
				f[1] = (float) s[i].getAngle();
				f[2] = 1f;
				f = c.toRGB(f);
				for (int j = 0; j < 3; j++)
					f[j] = Math.min(1, f[j]);
				g.setColor(new Color(f[0], f[1], f[2], .5f));
				g.fillRect(i * w / n, 0, w / n, 240);
			}
		}
	}

	private void drawTarget(Image img, TargetLocation data) {
		Graphics2D g = (Graphics2D) img.getGraphics();
		if (data == null) {
			g.drawString("No target", 0, 15);
			return;
		}
		Point p = data.getLocation();
		g.setColor(Color.red);
		g.fillOval(p.x, p.y, 10, 10);
		g.setColor(Color.white);
		g.drawOval(p.x, p.y, 10, 10);
	}
}
