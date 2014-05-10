package org.cen.vision.filters;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import org.cen.geom.Point2D;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.media.jai.ImageLayout;
import javax.media.jai.UntiledOpImage;

public class GrowingRegionOpImage extends UntiledOpImage {
	public static class Region {
		private int count;

		private int id;

		private float[] value = new float[3];

		private float[] variance = new float[3];

		private int x;

		private int y;

		private Point topLeft;

		private Point bottomRight;

		public Region(int id) {
			super();
			topLeft = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
			bottomRight = new Point(0, 0);
			this.id = id;
		}

		public void add(int x, int y, float[] f) {
			count++;
			this.x += x;
			this.y += y;
			for (int i = f.length; --i >= 0;) {
				value[i] += f[i];
				variance[i] += f[i] * f[i];
			}

			if (topLeft.x > x) {
				topLeft.x = x;
			}
			if (topLeft.y > y) {
				topLeft.y = y;
			}
			if (bottomRight.x < x) {
				bottomRight.x = x;
			}
			if (bottomRight.y < y) {
				bottomRight.y = y;
			}
		}

		public Rectangle getBounds() {
			return new Rectangle(topLeft.x, topLeft.y, bottomRight.x - topLeft.x + 1, bottomRight.y - topLeft.y + 1);
		}

		public int getCount() {
			return count;
		}

		public int getId() {
			return id;
		}

		public Point2D getLocation() {
			return new Point2D.Double(x / count, y / count);
		}

		public float[] getMeanValue() {
			float[] f = new float[value.length];
			for (int i = f.length; --i >= 0;) {
				f[i] = value[i] / count;
			}
			return f;
		}

		public String getName() {
			return "region" + id;
		}

		public float[] getVariance() {
			float[] m = getMeanValue();
			float[] f = new float[variance.length];
			for (int i = f.length; --i >= 0;) {
				f[i] = (variance[i] / count) - (m[i] * m[i]);
			}
			return f;
		}

		public void merge(Region region) {
			count += region.count;
			for (int i = value.length; --i >= 0;) {
				value[i] += region.value[i];
			}
			x += region.x;
			y += region.y;

			region.count = count;
			region.id = id;
			region.value = value;
			region.x = x;
			region.y = y;
		}
	}

	private class RegionDistance {
		private Region region1;

		private Region region2;

		private double distance;

		public RegionDistance(Region region1, Region region2, double distance) {
			super();
			this.region1 = region1;
			this.region2 = region2;
			this.distance = distance;
		}

		public void merge() {
			region1.merge(region2);
		}
	}

	public static final String KEY_REGIONCOUNT = "regionCount";

	private ColorSpace colorSpace = VHSColorSpace.getInstance();

	private int regionCount = 0;

	private List<Region> regions = new ArrayList<Region>();

	private double threshold;

	private FilterInterruptor interruptor;

	private int stepY;

	private int stepX;

	public GrowingRegionOpImage(Vector<?> sources, Map<?, ?> configuration, ImageLayout layout, double threshold, int stepX, int stepY, FilterInterruptor interruptor) {
		super(sources, configuration, layout);
		this.threshold = threshold;
		this.interruptor = interruptor;
		this.stepX = stepX;
		this.stepY = stepY;
	}

	@Override
	protected void computeImage(Raster[] sources, WritableRaster dest, Rectangle destRect) {
		final Raster source = sources[0];
		int w = source.getWidth();
		int h = source.getHeight();
		final float[] f1 = new float[3];
		final float[] f2 = new float[3];
		int[][] regionMap = new int[w][h];

		final double threshold = this.threshold;
		final int stepX = this.stepX;
		final int stepY = this.stepY;

		Deque<Integer> stack = new ArrayDeque<Integer>(w * h * 2);

		List<Integer> ll = new ArrayList<Integer>(w * h);
		for (int y = 0; y < h; y += stepY) {
			for (int x = 0; x < w; x += stepX) {
				ll.add((y << 16) | x);
			}
		}
		Collections.sort(ll, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				int i = o1.intValue();
				int x = i & 0xffff;
				int y = i >> 16;
				source.getPixel(x, y, f1);
				double d1 = getDistance(f1, f2);
				i = o2.intValue();
				x = i & 0xffff;
				y = i >> 16;
				source.getPixel(x, y, f1);
				double d2 = getDistance(f1, f2);
				return Double.compare(d1, d2);
			}
		});

		int l = ll.size();
		// for (int i = 1; i < l / 2; i += 2) {
		// Collections.swap(ll, i, l - i);
		// }

		while (l > 0 && !interruptor.isInterrupted()) {
			stack.add(ll.get(--l));
			while (!stack.isEmpty() && !interruptor.isInterrupted()) {
				Integer i = stack.removeFirst();
				int v = i.intValue();
				int xx = v & 0xffff;
				int yy = v >> 16;

				Region r;
				if (regionMap[xx][yy] <= 0) {
					r = new Region(++regionCount);
					regions.add(r);
					source.getPixel(xx, yy, f1);
					r.add(xx, yy, f1);
					regionMap[xx][yy] = regionCount;
				} else {
					r = regions.get(regionMap[xx][yy] - 1);
				}

				for (int dy = -1; dy <= 1; dy++) {
					for (int dx = -1; dx <= 1; dx++) {
						int rx = xx + dx;
						int ry = yy + dy;
						if ((rx < 0) || (ry < 0) || (ry >= h) || (rx >= w)) {
							continue;
						}
						if (regionMap[rx][ry] <= 0) {
							source.getPixel(rx, ry, f2);
							double var = Math.abs((r.variance[0] + f2[0] * f2[0]) / (r.count + 1) - Math.pow((r.value[0] + f2[0]) / (r.count + 1), 2));
							if (var < threshold) {
								stack.offerLast(Integer.valueOf((ry << 16) | rx));
								regionMap[rx][ry] = r.id;
								r.add(rx, ry, f2);
							}
						}
					}
				}
			}
		}

		if (interruptor.isInterrupted()) {
			System.out.println("interrupted");
			return;
		}

		System.out.println("RegionCount=" + regionCount);

		mergeRegions();

		setProperty(KEY_REGIONCOUNT, regionCount);
		int i = 0;
		for (Region r : regions) {
			i++;
			setProperty("region" + i, r);
		}

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				i = regionMap[x][y] - 1;
				if (i < 0) {
					continue;
				}
				Region r = regions.get(i);
				dest.setPixel(x, y, r.getMeanValue());
			}
		}
	}

	private float getDistance(float[] f1, float[] f2) {
		float[] v1 = colorSpace.fromRGB(f1);
		float[] v2 = colorSpace.fromRGB(f2);
		// return (float) Angle.getDistance(v1[1], v2[1]);
		return Math.abs(v1[0] - v2[0]);
	}

	private void mergeRegions() {
		// List<RegionDistance> l = new ArrayList<RegionDistance>();
		List<Region> r = new ArrayList<Region>(regions);
		while (regionCount > 50) {
			int n = r.size();
			RegionDistance rmin = null;
			double dmin = Double.MAX_VALUE;
			for (int i = n; i-- > 0;) {
				Region ri = r.get(i);
				for (int j = i; j-- > 0;) {
					Region rj = r.get(j);
					double d = getDistance(ri.getMeanValue(), rj.getMeanValue()) * ri.getLocation().distance(rj.getLocation()) * ri.getCount();
					if (dmin > d) {
						rmin = new RegionDistance(ri, rj, d);
						dmin = d;
					}
					// l.add(new RegionDistance(ri, rj, d));
				}
			}
			// Collections.sort(l, new Comparator<RegionDistance>() {
			// @Override
			// public int compare(RegionDistance r1, RegionDistance r2) {
			// return Double.compare(r1.distance, r2.distance);
			// }
			// });
			rmin.merge();
			r.remove(rmin.region2);
			regionCount--;
		}
	}
}
