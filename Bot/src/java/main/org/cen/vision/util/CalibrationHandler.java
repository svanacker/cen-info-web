package org.cen.vision.util;

import java.awt.Image;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.swing.event.EventListenerList;

import org.cen.math.Angle;
import org.cen.vision.Acquisition;
import org.cen.vision.dataobjects.CalibrationData;
import org.cen.vision.dataobjects.CalibrationDescriptor;
import org.cen.vision.filters.CalibrationStat;
import org.cen.vision.filters.CalibrationStat.Sample;

/**
 * Object responsible for the color calibration of the webcam.
 * 
 * @author Emmanuel ZURMELY
 */
public class CalibrationHandler {
	/**
	 * Object responsible for analyzing the calibration statistics through the
	 * time.
	 * 
	 * @author Emmanuel ZURMELY
	 */
	protected class CalibrationStatAnalyzer {
		/**
		 * Descriptor of the required calibration data.
		 */
		protected CalibrationDescriptor descriptor;

		private int[] done;

		/**
		 * Statistics history.
		 */
		protected CalibrationStat[] history;

		private int n = 0, matching = 0;

		private boolean requiredData = false;

		private double[] values;

		/**
		 * Constructor.
		 * 
		 * @param descriptor
		 *            descriptor of the required calibration data
		 */
		public CalibrationStatAnalyzer(CalibrationDescriptor descriptor) {
			super();
			this.descriptor = descriptor;
			history = new CalibrationStat[historySize];
			values = new double[historySize];
			done = new int[bandsCount];
			for (int i = bandsCount; i-- > 0;)
				done[i] = -1;
		}

		/**
		 * Adds the statistics to the history of data to analyze.
		 * 
		 * @param stats
		 *            the calibration statistics to add to the history
		 */
		public void addStats(CalibrationStat stats) {
			history[n++] = stats;
			if (n >= historySize) {
				requiredData = true;
				n = 0;
			}
			// Réinitialisation des bandes marquées comme traitées après
			// renouvellement de l'historique
			for (int i = bandsCount; i-- > 0;)
				if (done[i] == n)
					done[i] = -1;
		}

		/**
		 * Analyzes the statistics history to find a stable band matching the
		 * required calibration data given by the calibration descriptor.
		 */
		public void analyze() {
			if (!requiredData)
				// Pas assez de données dans l'historique
				return;
			// Boucle sur les bandes
			bands: for (int i = 0; i < bandsCount; i++) {
				if (done[i] >= 0)
					// Bande suivante
					continue;
				// Boucle sur l'historique
				for (int j = 0; j < historySize; j++) {
					Sample[] sample = history[j].getSamples();
					// Analyse de l'homogénéité spaciale de la bande
					if (sample[i].getDistance() > distanceThreshold)
						// Bande suivante
						continue bands;
					values[j] = sample[i].getAngle();
				}
				// Analyse de l'homogénéité temporelle de la bande
				if (Angle.getVariation(values) > distanceThreshold)
					// Bande suivante
					continue;
				// Recherche d'une donnée de calibration correspondant aux
				// caractéristiques de la bande analysée
				if (isMatchingBand(i)) {
					// La bande ne sera plus analysée par la suite
					done[i] = n;
					// Incrémentation du compteur de bandes exploitables prises
					// en compte
					matching++;
				}
			}
		}

		/**
		 * Returns the current number of bands matching the calibration
		 * descriptor.
		 * 
		 * @return the current number of bands matching the calibration
		 *         descriptor.
		 */
		public int getMatching() {
			return matching;
		}

		private boolean isMatchingBand(int bandId) {
			// Parcourt les données du descripteur pour trouver un critère de
			// calibration correspondant à la bande analysée
			for (CalibrationData d : descriptor.getData()) {
				if (!Double.isNaN(d.getResult()))
					// Le critère a déjà été attribué à une bande
					continue;
				// Moyenne temporelle de la couleur de la bande
				double angle = Angle.getMean(values);
				// Distance entre la bande analysée et le critère de calibration
				// souhaitée
				double distance = Angle.getDistance(angle, d.getExpected());
				// Vérification si la distance est inférieure à l'erreur à
				// tolérer
				if (distance <= d.getError()) {
					// Affectation de la bande au critère
					d.setResult(angle);
					System.out.println("calibration match: band=" + bandId + " -> " + d);
					notifyBandFound(d);
					// Un critère correspondant à la bande a été trouvé
					return true;
				}
			}
			// Aucun critère de calibration ne correspond à la bande
			return false;
		}
	}

	/**
	 * Object responsible for images acquisition.
	 */
	protected Acquisition acquisition;

	/**
	 * Number of calibration bands.
	 */
	protected int bandsCount = 16;

	/**
	 * Delay in milliseconds between two acquisition.
	 */
	protected int delay = 250;

	/**
	 * Maximum variation in radians between colours to consider that they are
	 * homogenous.
	 */
	protected double distanceThreshold = .15;

	/**
	 * Number of statistics used for temporal analysis.
	 */
	protected int historySize = 5;

	private EventListenerList listeners = new EventListenerList();

	private int zoneMin;

	private int zoneMax;

	/**
	 * Constructor.
	 * 
	 * @param acquisition
	 *            object responsible for image acquisition
	 */
	public CalibrationHandler(Acquisition acquisition) {
		super();
		this.acquisition = acquisition;
	}

	/**
	 * Adds a debug listener to this calibration handler.
	 * 
	 * @param listener
	 *            the listener to add
	 */
	public void addDebugListener(CalibrationDebugListener listener) {
		listeners.add(CalibrationDebugListener.class, listener);
	}

	/**
	 * Adds a band listener to this calibration handler.
	 * 
	 * @param listener
	 *            the listener to add
	 */
	public void addBandListener(CalibrationBandListener listener) {
		listeners.add(CalibrationBandListener.class, listener);
	}

	/**
	 * Starts the calibration using the specified calibration descriptor.
	 * 
	 * @param descriptor
	 *            the data describing the resuired calibration
	 * @return true if the calibration has been done successfully, false if the
	 *         calibration has timed out
	 */
	public boolean execute(CalibrationDescriptor descriptor) {
		CalibrationStatAnalyzer analyzer = new CalibrationStatAnalyzer(descriptor);
		long start = System.currentTimeMillis();
		// Compteur de tentatives
		int tries = 0;
		// Nombre de critères de calibration à satisfaire
		int required = descriptor.getData().length;
		while (tries++ < descriptor.getMaxRetryCount() && System.currentTimeMillis() - start < descriptor.getMaxCalibrationTime()) {
			// Acquisition
			Image img = acquisition.getImage();

			// Analyse spatiale de l'image
			ParameterBlock pb = new ParameterBlock();
			pb.addSource(img);
			pb.add(bandsCount);
			pb.add(3);
			pb.add(zoneMin);
			pb.add(zoneMax);
			// Test sur la mire FreeBox
			// pb.add(85);
			// pb.add(90);
			RenderedOp s = JAI.create("CalibrationFilter", pb, null);
			CalibrationStat stats = (CalibrationStat) s.getProperty("Calibration");

			// Débogage
			notify(img, stats);

			// Analyse temporelle
			analyzer.addStats(stats);
			analyzer.analyze();

			if (analyzer.getMatching() >= required)
				return true;

			// Attente du cycle suivant
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
				// Le thread a été interrompu
				break;
			}
		}
		return false;
	}

	/**
	 * Returns the number of calibration bands.
	 * 
	 * @return the calibration bands count
	 */
	public int getBandsCount() {
		return bandsCount;
	}

	/**
	 * Returns the delay between two acquisitions.
	 * 
	 * @return the delay between two acquisitions in milliseconds
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * Returns the maximum distance between colours used to consider that these
	 * colours are homogenous.
	 * 
	 * @param distanceThreshold
	 *            the angular distance in radians
	 */
	public double getDistanceThreshold() {
		return distanceThreshold;
	}

	/**
	 * Returns the history size used for temporal analysis.
	 * 
	 * @return the history size
	 */
	public int getHistorySize() {
		return historySize;
	}

	/**
	 * Notifies the listeners.
	 * 
	 * @param img
	 *            the acquired image
	 * @param stats
	 *            the calibration statistics for the acquired image
	 */
	protected void notifyBandFound(CalibrationData calibrationData) {
		Object[] listeners = this.listeners.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == CalibrationBandListener.class) {
				((CalibrationBandListener) listeners[i + 1]).bandFound(calibrationData);
			}
		}
	}

	/**
	 * Notifies the listeners that a band was found.
	 * 
	 * @param img
	 *            the acquired image
	 * @param stats
	 *            the calibration statistics for the acquired image
	 */
	protected void notify(Image img, CalibrationStat stats) {
		Object[] listeners = this.listeners.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == CalibrationDebugListener.class) {
				((CalibrationDebugListener) listeners[i + 1]).debug(img, stats);
			}
		}
	}

	/**
	 * Removes the specified listener from the list of listeners.
	 * 
	 * @param listener
	 *            the listener to remove
	 */
	public void removeDebugListener(CalibrationDebugListener listener) {
		listeners.remove(CalibrationDebugListener.class, listener);
	}

	/**
	 * Removes the specified listener from the list of listeners.
	 * 
	 * @param listener
	 *            the listener to remove
	 */
	public void removeDebugListener(CalibrationBandListener listener) {
		listeners.remove(CalibrationBandListener.class, listener);
	}

	/**
	 * Sets the number of calibration bands.
	 * 
	 * @param bandsCount
	 *            the calibration bands count
	 */
	public void setBandsCount(int bandsCount) {
		this.bandsCount = bandsCount;
	}

	/**
	 * Sets the delay between two acquisitions.
	 * 
	 * @param delay
	 *            the delay between two acquisitions in milliseconds
	 */
	public void setDelay(int delay) {
		this.delay = delay;
	}

	/**
	 * Sets the maximum distance between colours to consider that the colours
	 * are homogenous.
	 * 
	 * @param distanceThreshold
	 *            the angular distance in radians
	 */
	public void setDistanceThreshold(double distanceThreshold) {
		this.distanceThreshold = distanceThreshold;
	}

	/**
	 * Sets the size of the calibration statistics history.
	 * 
	 * @param historySize
	 *            the size of the statistics history
	 */
	public void setHistorySize(int historySize) {
		this.historySize = historySize;
	}

	public void setCalibrationZone(int min, int max) {
		zoneMin = min;
		zoneMax = max;
	}
}