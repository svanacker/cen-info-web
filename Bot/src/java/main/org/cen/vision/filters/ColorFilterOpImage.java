package org.cen.vision.filters;

import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Map;
import java.util.Vector;

import javax.media.jai.ImageLayout;
import javax.media.jai.UntiledOpImage;

import org.cen.math.Angle;

/**
 * Opérateur filtre de couleur.
 * 
 * @author Emmanuel ZURMELY
 */
public class ColorFilterOpImage extends UntiledOpImage {
	private double angle;

	private float[] f;

	private FilterInterruptor interruptor;

	public ColorFilterOpImage(Vector<?> sources, Map<?, ?> configuration, ImageLayout layout, double angle, float[] f, FilterInterruptor interruptor) {
		super(sources, configuration, layout);
		this.angle = angle;
		this.f = f;
		this.interruptor = interruptor;
	}

	@Override
	protected void computeImage(Raster[] sources, WritableRaster dest, Rectangle destRect) {
		// Construit une image des distances angulaires des couleurs de la
		// source à la couleur du filtre (0 = angle 2 Pi rad, 255 = angle 0 rad)
		Raster src = sources[0];
		int w = src.getWidth();
		int h = src.getHeight();
		assert (src.getNumBands() == 3) : "getNumBands() != 3";
		float[] f = this.f;
		if (f == null) {
			f = new float[w * h * src.getNumBands()];
		}
		float[] p = new float[3];
		f = src.getPixels(0, 0, w, h, f);
		ColorSpace c = VHSColorSpace.getInstance();
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				int n = (y * w + x) * 3;
				for (int i = 0; i < 3; i++)
					p[i] = f[n + i];
				p = c.fromRGB(p);
				p[0] = (float) getDistance(p[1], angle);
				f[n] = Math.max(0, 255 - 2 * (float) (p[0] * 255 / Math.PI));
				if (f[n] < 128 || p[2] < .05) {
					f[n] = 0;
				}
				f[n + 1] = f[n];
				f[n + 2] = f[n];

				if (interruptor.isInterrupted()) {
					return;
				}
			}
		}
		dest.setPixels(0, 0, w, h, f);
	}

	private double getDistance(float f, double d) {
		// Distance angulaire entre la couleur d'un point et la couleur du
		// filtre
		return Angle.getDistance(f, d);
	}
}
