package org.cen.vision.filters;

import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;

import javax.media.jai.StatisticsOpImage;

public class ObjectsConfigurationOpImage extends StatisticsOpImage {
	public static class ObjectsConfigurationStat {
		public static enum Color {
			BLUE, RED, WHITE;
		}

		private double angle;

		private double length;

		private Rectangle rectangle;

		public ObjectsConfigurationStat(String name, Rectangle rectangle) {
			super();
			this.rectangle = rectangle;
		}

		/**
		 * @return the angle
		 */
		public double getAngle() {
			return angle;
		}

		public Color getColor() {
			if (length < .1)
				return Color.WHITE;
			else if (angle > Math.PI / 2d && angle < 3d * Math.PI / 2d)
				return Color.BLUE;
			else
				return Color.RED;
		}

		/**
		 * @return the length
		 */
		public double getLength() {
			return length;
		}

		public int getSize() {
			return rectangle.width * rectangle.height;
		}

		protected void setResult(double angle, double length) {
			this.angle = angle;
			this.length = length;
		}

		@Override
		public String toString() {
			return getClass().getSimpleName() + "[color=" + getColor() + ", angle=" + angle + ", length=" + length + ", rect=" + rectangle + "]";
		}
	}

	private Rectangle r1;

	private Rectangle r2;

	private Rectangle r3;

	public ObjectsConfigurationOpImage(RenderedImage source, Rectangle r1, Rectangle r2, Rectangle r3) {
		super(source, null, 0, 0, 1, 1);
		this.r1 = r1;
		this.r2 = r2;
		this.r3 = r3;
	}

	@Override
	protected void accumulateStatistics(String name, Raster src, Object statsObject) {
		ColorSpace cs = VHSColorSpace.getInstance();
		ObjectsConfigurationStat stat = (ObjectsConfigurationStat) statsObject;
		float[] rgb = new float[3];
		float[] f = src.getPixels(stat.rectangle.x, stat.rectangle.y, stat.rectangle.width, stat.rectangle.height, (float[]) null);
		double mx = 0, my = 0;
		for (int i = 0; i < f.length; i += 3) {
			for (int j = 0; j < rgb.length; j++)
				rgb[j] = f[i + j];
			float[] vhs = cs.fromRGB(rgb);
			// Moyenne des vecteurs couleurs de norme saturation
			mx += Math.cos(vhs[1]) * vhs[2];
			my += Math.sin(vhs[1]) * vhs[2];
		}
		double angle = Math.atan2(my, mx);
		if (angle < 0)
			angle += Math.PI * 2d;
		double length = Math.sqrt(mx * mx + my * my) / stat.getSize();
		// Angle et norme moyens
		stat.setResult(angle, length);
	}

	@Override
	protected Object createStatistics(String name) {
		if (name.equals("Position1"))
			return new ObjectsConfigurationStat(name, r1);
		else if (name.equals("Position2"))
			return new ObjectsConfigurationStat(name, r2);
		else if (name.equals("Position3"))
			return new ObjectsConfigurationStat(name, r3);
		else
			return null;
	}

	@Override
	protected String[] getStatisticsNames() {
		return new String[] { "Position1", "Position2", "Position3" };
	}
}
