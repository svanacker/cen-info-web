package org.cen.vision.filters;

import java.awt.color.ColorSpace;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;

import javax.media.jai.StatisticsOpImage;

import org.cen.math.Angle;

public class TargetFilterOpImage extends StatisticsOpImage {
	private static int n = 5;

	private double filteredColor;

	private double intensityThreshold;

	private double[][][] mag;

	private double[] rx = new double[n];

	private double[] ry = new double[n];

	private double saturationThreshold;

	private double slope;

	private TargetStat stats = new TargetStat();

	public TargetFilterOpImage(RenderedImage source, double filteredColor, double saturationThreshold, double intensityThreshold, double slope) {
		super(source, null, 0, 0, 1, 1);
		this.filteredColor = filteredColor;
		this.saturationThreshold = saturationThreshold;
		this.intensityThreshold = intensityThreshold;
		this.slope = slope;
		stats = new TargetStat();
		stats.setSize(n);
		stats.setName("Magnitude");
	}

	@Override
	protected void accumulateStatistics(String name, Raster src, Object statsObject) {
		ColorSpace c = VHSColorSpace.getInstance();
		if (mag == null) {
			int w = src.getWidth();
			int h = src.getHeight();
			mag = new double[n][][];
			int[][][] weights = new int[n][][];
			for (int i = 0, j = 2; i < n; i++, j *= 2) {
				mag[i] = new double[j][j];
				weights[i] = new int[j][j];
				rx[i] = (double) w / j;
				ry[i] = (double) h / j;
			}
			float[] p = new float[3];
			for (int x = 0; x < w; x++)
				for (int y = 0; y < h; y++) {
					p = src.getPixel(x, y, p);
					for (int i = 0; i < n; i++) {
						int tx = (int) (x / rx[i]);
						int ty = (int) (y / ry[i]);
						mag[i][tx][ty] += getMagnitude(c, p);
						weights[i][tx][ty]++;
					}
				}
			for (int i = 0; i < n; i++)
				for (int x = 0; x < weights[i].length; x++)
					for (int y = 0; y < weights[i][x].length; y++)
						mag[i][x][y] /= weights[i][x][y];
			stats.setData(mag);
		}
	}

	@Override
	protected Object createStatistics(String name) {
		if (stats.getName().equals(name))
			return stats;
		return null;
	}

	private double getMagnitude(ColorSpace c, float[] p) {
		p = c.fromRGB(p);
		double m = 1 - slope * Angle.getDistance(p[1], filteredColor) / Math.PI;
		if (m < intensityThreshold || p[2] < saturationThreshold)
			m = 0;
		return m;
	}

	@Override
	protected String[] getStatisticsNames() {
		return new String[] { "Magnitude" };
	}
}
