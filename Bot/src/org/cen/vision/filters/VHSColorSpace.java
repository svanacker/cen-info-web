package org.cen.vision.filters;

import java.awt.color.ColorSpace;

public class VHSColorSpace extends ColorSpace {
	private static VHSColorSpace instance;

	public static VHSColorSpace getInstance() {
		if (instance == null)
			synchronized (VHSColorSpace.class) {
				if (instance == null)
					instance = new VHSColorSpace();
			}
		return instance;
	}

	protected VHSColorSpace() {
		super(ColorSpace.TYPE_HSV, 3);
	}

	@Override
	public float[] fromCIEXYZ(float[] colorvalue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float[] fromRGB(float[] rgbvalue) {
		float[] f = new float[3];
		float min = Math.min(Math.min(rgbvalue[0], rgbvalue[1]), rgbvalue[2]);
		float max = Math.max(Math.max(rgbvalue[0], rgbvalue[1]), rgbvalue[2]);
		// Value
		f[0] = (rgbvalue[0] + rgbvalue[1] + rgbvalue[2]) / 765f;
		// Hue
		if (min == max)
			f[1] = 0;
		else if (rgbvalue[0] == max && rgbvalue[1] >= rgbvalue[2])
			f[1] = (float) ((Math.PI / 3d) * (rgbvalue[1] - rgbvalue[2]) / (max - min));
		else if (rgbvalue[0] == max && rgbvalue[1] < rgbvalue[2])
			f[1] = (float) ((Math.PI / 3d) * (rgbvalue[1] - rgbvalue[2]) / (max - min) + (2d * Math.PI));
		else if (rgbvalue[1] == max)
			f[1] = (float) ((Math.PI / 3d) * (rgbvalue[2] - rgbvalue[0]) / (max - min) + (2d * Math.PI / 3d));
		else if (rgbvalue[2] == max)
			f[1] = (float) ((Math.PI / 3d) * (rgbvalue[0] - rgbvalue[1]) / (max - min) + (4d * Math.PI / 3d));
		// Saturation
		f[2] = (max == 0) ? 0 : 1f - (min / max);
		return f;
	}

	@Override
	public float[] toCIEXYZ(float[] colorvalue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float[] toRGB(float[] colorvalue) {
		// TODO Auto-generated method stub
		return null;
	}
}
