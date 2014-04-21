package org.cen.vision.filters;

import java.awt.color.ColorSpace;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;

import javax.media.jai.StatisticsOpImage;

import org.cen.math.Angle;

/**
 * Statistics operator providing information used to calibrate the color filter.
 * 
 * @author Emmanuel ZURMELY
 */
public class CalibrationFilterOpImage extends StatisticsOpImage {
	// Statistics' name
	public static final String NAME = "Calibration";

	// Number of horizontal bands
	private int nx;

	// Number of horizontal bands
	private int ny;

	// Color saturation threshold
	private double saturationThreshold;

	// Maximum x coordinate
	private int xmax = Integer.MAX_VALUE;

	// Minimum x coordinate
	private int xmin = 0;

	// Maximum y coordinate
	private int ymax = Integer.MAX_VALUE;

	// Minimum y coordinate
	private int ymin = 0;

	public CalibrationFilterOpImage(RenderedImage source, int hBands, int vBands, int ymin, int ymax, int xmin, int xmax, double saturationThreshold) {
		super(source, null, 0, 0, 1, 1);
		ny = vBands;
		nx = hBands;
		this.xmin = xmin;
		this.xmax = xmax;
		this.ymin = ymin;
		this.ymax = ymax;
		this.saturationThreshold = saturationThreshold;
	}

	@Override
	protected void accumulateStatistics(String name, Raster src, Object statsObject) {
		CalibrationStat stats = (CalibrationStat) statsObject;
		CalibrationStat.Sample[] samples = new CalibrationStat.Sample[nx];
		ColorSpace c = VHSColorSpace.getInstance();
		float[] f = new float[3];
		int w = Math.min(getWidth(), xmax) - xmin;
		int h = Math.min(getHeight(), ymax) - ymin;
		// Taille des bandes horizontales en pixels
		int dx = w / nx;
		// Nombre de pixels analysés
		int size = dx * ny;
		// Tableau des valeurs d'angle
		double[] v = new double[size];
		// Boucle sur les bandes horizontales
		for (int n = 0; n < nx; n++) {
			int i = 0;
			// Vecteur moyenne
			double mx = 0, my = 0;
			// Boucle sur les pixels selon x
			for (int x = n * dx + xmin; x < (n + 1) * dx + xmin; x++) {
				// Boucle sur les bandes verticales
				int dy = (h + ny) / (ny + 1);
				for (int y = dy + ymin; y < h + ymin; y += dy) {
					f = src.getPixel(x, y, f);
					// changement de repère RGB -> IHS
					f = c.fromRGB(f);
					// test du seuil de saturation
					if (f[2] < saturationThreshold)
						continue;
					// angle
					double angle = f[1];
					// tableau des valeurs
					v[i++] = angle;
					// moyenne des vecteurs
					mx += Math.cos(angle);
					my += Math.sin(angle);
				}
			}

			// s'il n'y a pas assez de pixels significatifs dans l'échantillon,
			// on l'abandonne
			if (i * 3 < size * 2) {
				samples[n] = stats.new Sample(0, Double.MAX_VALUE);
				continue;
			}

			// angle moyen à partir du vecteur moyen
			double m = Math.atan2(my, mx);
			if (m < 0)
				m = 2 * Math.PI + m;

			// distance à la moyenne
			double var = 0;
			for (int j = 0; j < i; j++)
				var += Angle.getDistance(v[j], m);

			samples[n] = stats.new Sample(m, var / i);
		}
		stats.setSamples(samples);
	}

	@Override
	protected Object createStatistics(String name) {
		if (NAME.equals(name))
			return new CalibrationStat();
		return null;
	}

	@Override
	protected String[] getStatisticsNames() {
		return new String[] { NAME };
	}
}
