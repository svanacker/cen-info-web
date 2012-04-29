package org.cen.vision.filters;

import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.media.jai.StatisticsOpImage;

public class HoughTransformOpImage extends StatisticsOpImage {
	private static final String LINES = "lines";

	private static final String[] NAMES = { LINES };

	// accumulators array
	private int[][] acc;

	// 8 neighboors offsets
	private int[] dx8 = new int[] { -1, 0, 1, 1, 1, 0, -1, -1 };

	private int[] dy8 = new int[] { -1, -1, -1, 0, 1, 1, 1, 0 };

	// size of the accumulators array
	private int maxIndexTheta, maxIndexRho;

	// max Rho walue (= length of the diagonal)
	private double maxRho;

	// max Theta walue (= 180 degrees = PI)
	private double maxTheta = Math.PI;

	private int radius = 6;

	private double threshold = 0.5d;

	public HoughTransformOpImage(RenderedImage source, double threshold, int radius) {
		super(source, null, 0, 0, 1, 1);
		this.threshold = threshold;
		this.radius = radius;
	}

	@Override
	protected void accumulateStatistics(String name, Raster source, Object stats) {
		if (name.equals(LINES)) {
			int w = source.getWidth();
			int h = source.getHeight();

			// diagonal
			maxRho = Math.sqrt(w * w + h * h);
			// size of the accumulators array
			maxIndexTheta = 360; // precision : 180/360 = 0.5 degree
			maxIndexRho = (int) (1 + maxRho); // precision : 1 pixel
			acc = new int[maxIndexRho][maxIndexTheta];

			int[][] gray = new int[w][h];
			int[] p = new int[source.getNumBands()];
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					p = source.getPixel(x, y, p);
					gray[x][y] = (299 * p[0] + 587 * p[1] + 114 * p[2]) / 1000;
				}
			}

			// compute gradient (Sobel) + vote in Hough Space (if gradient>64)
			for (int y = 1; y < h - 1; y++) {
				for (int x = 1; x < w - 1; x++) {
					int gv = (gray[x + 1][y - 1] - gray[x - 1][y - 1]) + 2 * (gray[x + 1][y] - gray[x - 1][y]) + (gray[x + 1][y + 1] - gray[x - 1][y + 1]);
					int gh = (gray[x - 1][y + 1] - gray[x - 1][y - 1]) + 2 * (gray[x][y + 1] - gray[x][y - 1]) + (gray[x + 1][y + 1] - gray[x + 1][y - 1]);
					int g2 = (gv * gv + gh * gh) / (4);
					if (g2 > 4096) {
						vote(x, y, w, h); // ||gradient||^2 > 64^2
					}
				}
			}

			getWinners((List<HoughLine>) stats, w, h);
		}
	}

	@Override
	protected Object createStatistics(String name) {
		if (name.equals(LINES)) {
			return new ArrayList<HoughLine>();
		}
		return null;
	}

	// minimum distance between 2 coords in the array (mobius)
	private int distance(int r0, int t0, int r1, int t1) {
		/* distance between (r0,t0) and (r1,t1) */
		int dist = Math.max(Math.abs(r0 - r1), Math.abs(t0 - t1));

		if (t0 < t1) {
			/* distance between (-r0,t0+PI) and (r1,t1) */
			t0 = t0 + maxIndexTheta; // theta=theta+PI => tindex=tindex+maxindex
			r0 = maxIndexRho - r0 - 1; // rho=-rho => rindex=max-rindex
		} else {
			/* distance between (r0,t0) and (-r1,t1+PI) */
			t1 = t1 + maxIndexTheta; // theta=theta+PI => tindex=tindex+maxindex
			r1 = maxIndexRho - r1 - 1; // rho=-rho => rindex=max-rindex
		}
		dist = Math.min(dist, Math.max(Math.abs(r0 - r1), Math.abs(t0 - t1)));

		return dist;
	}

	@Override
	protected String[] getStatisticsNames() {
		return NAMES;
	}

	// compute list of local extrema in Hough Space
	public void getWinners(List<HoughLine> winners, int w, int h) {
		// maximum in the array
		int highestvote = 0;
		for (int r = 0; r < maxIndexRho; r++) {
			for (int t = 0; t < maxIndexTheta; t++) {
				if (acc[r][t] > highestvote) {
					highestvote = acc[r][t];
				}
			}
		}

		// minimum vote needed to be a local extrema
		int minvote = (int)(highestvote * threshold);

		// parsing the accumulators
		List<int[]> coords = new ArrayList<int[]>();
		for (int r = 0; r < maxIndexRho; r++) {
			for (int t = 0; t < maxIndexTheta; t++) {

				// value of current accumulator
				if (acc[r][t] < minvote) {
					continue;
				}

				// maxima in the neighborhood of this accumulator
				int nmax = 0;
				for (int k = 0; k < dx8.length; k++) {
					int rk = r + dx8[k];
					int tk = t + dy8[k];

					if (rk < 0) {
						continue;
					}
					if (rk >= maxIndexRho) {
						continue;
					}
					if (tk < 0) {
						tk += maxIndexTheta;
					}
					if (tk >= maxIndexTheta) {
						tk -= maxIndexTheta;
					}

					if (acc[rk][tk] > nmax) {
						nmax = acc[rk][tk];
					}
				}

				// the current accumulator is not the highest value -> ignore
				if (nmax > acc[r][t]) {
					continue;
				}

				// prevent extrema to be too close to each others
				// => compare this coord to the others
				boolean ignore = false;
				Iterator<int[]> iter = coords.iterator();
				while (iter.hasNext()) {
					int[] coord = iter.next();
					int dist = distance(coord[0], coord[1], r, t);
					if (dist > (2 * radius)) {
						continue;
					}

					// this extrema is too close from the current one.
					// We keep the extrema with the highest vote.

					if (acc[r][t] >= acc[coord[0]][coord[1]]) {
						iter.remove(); // remove the other
					} else {
						ignore = true;
						break; // remove me
					}
				}

				// store extrema in the array
				if (!ignore) {
					coords.add(new int[] { r, t });
				}
			}
		}

		// convert array index to real (rho,theta) values
		for (int[] coord : coords) {
			int r = coord[0];
			int t = coord[1];

			// convert to real (rho,theta) value
			double rho = IndexToRho(r);
			double theta = IndexToTheta(t);

			// store in the list
			winners.add(new HoughLine(rho, theta, w, h));
		}
	}

	public double IndexToRho(int index) {
		return ((double) index / this.maxIndexRho - 0.5) * this.maxRho;
	}

	public double IndexToTheta(int index) {
		return ((double) index / this.maxIndexTheta) * this.maxTheta;
	}

	// convert rho[-maxRho,maxRho] and theta[0,maxTheta] from/to array index
	// [0,maxIndex]
	public int RhoToIndex(double rho) {
		return (int) (0.5 + (rho / this.maxRho + 0.5) * this.maxIndexRho);
	}

	public int ThetaToIndex(double theta) {
		return (int) (0.5 + (theta / this.maxTheta) * maxIndexTheta);
	}

	public void vote(int x, int y, int w, int h) {
		// centered
		x -= w / 2;
		y -= h / 2;

		for (int indexTheta = 0; indexTheta < maxIndexTheta; indexTheta += 1) {
			double theta = IndexToTheta(indexTheta);
			double rho = x * Math.cos(theta) + y * Math.sin(theta);

			// (rho,theta) -> index
			int indexRho = RhoToIndex(rho);

			if (indexTheta < maxIndexTheta && indexRho < maxIndexRho) {
				acc[indexRho][indexTheta]++;
			}
		}
	}
}
