package org.cen.vision.util;

import java.awt.Image;
import java.awt.Point;
import org.cen.geom.Point2D;
import java.awt.image.renderable.ParameterBlock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.swing.event.EventListenerList;

import org.cen.vision.Acquisition;
import org.cen.vision.coordinates.CoordinatesTransform;
import org.cen.vision.filters.TargetStat;
import org.cen.vision.filters.TargetStat.TargetLocation;

public class TargetHandler {
	public final static int GRID_SIZE = 32;

	private Acquisition acquisition;

	protected List<TargetLocation> bestTargets = new ArrayList<TargetLocation>();

	protected double filteredColor;

	protected double[][] historyGrid;

	protected double intensityThreshold = .7;

	private EventListenerList listeners = new EventListenerList();

	protected int maxTryCount = 20;

	protected double newDataRatio = 1.2;

	protected double oldDataRatio = .4;

	private double saturationThreshold = .1;

	protected double slope = 4d;

	protected CoordinatesTransform transform;

	protected int tries = 0;

	protected double weightThreshold = 1.5;

	/**
	 * Constructor.
	 * 
	 * @param acquisition
	 *            the image acquisition service
	 */
	public TargetHandler(Acquisition acquisition) {
		super();
		this.acquisition = acquisition;
		// Historique des donn�es
		historyGrid = new double[GRID_SIZE][GRID_SIZE];
	}

	/**
	 * Adds a debug listener to the listeners list of this object.
	 * 
	 * @param listener
	 *            the listener to add
	 */
	public void addDebugListener(TargetDebugListener listener) {
		listeners.add(TargetDebugListener.class, listener);
	}

	/**
	 * Adds a target listener to the listeners list of this object.
	 * 
	 * @param listener
	 *            the listener to add
	 */
	public void addTargetListener(TargetListener listener) {
		listeners.add(TargetListener.class, listener);
	}

	protected void analyze() {
		bestTargets.clear();

		for (int x = 0; x < historyGrid.length; x++) {
			double max = 0;
			int mx = 0, my = 0;
			for (int y = 0; y < historyGrid[x].length; y++) {
				if (historyGrid[x][y] > max) {
					max = historyGrid[x][y];
					mx = x;
					my = y;
				}
			}

			Point location = new Point(mx, my);
			// Game board coordinates
			if (transform != null) {
				// Screen coordinates
				// Target location in screen coordinates
				Point2D position = transform.screenToGameBoard(location.x, location.y);
				if (max >= weightThreshold)
					notifyTarget(position, max);
				else if (tries++ > maxTryCount)
					notifyFailure();
			}
			TargetLocation target = new TargetLocation(location, max);
			bestTargets.add(target);
		}
		Collections.sort(bestTargets);
	}

	public void clearHistory() {
		for (int x = 0; x < historyGrid.length; x++)
			for (int y = 0; y < historyGrid[x].length; y++)
				historyGrid[x][y] = 0;
	}

	/**
	 * Returns the last best matching target detected.
	 * 
	 * @return the last best matching target detected
	 */
	public TargetLocation getBestTarget(int index) {
		return bestTargets.get(index);
	}

	/**
	 * Returns the color filtered by the filter.
	 * 
	 * @return the filtered color (hue angle in radians)
	 */
	public double getFilteredColor() {
		return filteredColor;
	}

	/**
	 * Returns the intensity threshold applied by the filter.
	 * 
	 * @return the intensity threshold applied by the filter
	 */
	public double getIntensityThreshold() {
		return intensityThreshold;
	}

	/**
	 * Returns the maximum number of tries to detect a target before notifying a
	 * failure.
	 * 
	 * @return the maximum number of tries
	 */
	public int getMaxTryCount() {
		return maxTryCount;
	}

	public double getNewDataRatio() {
		return newDataRatio;
	}

	public double getOldDataRatio() {
		return oldDataRatio;
	}

	/**
	 * Returns the saturation threshold applied by the filter.
	 * 
	 * @return the saturation threshold applied by the filter
	 */
	public double getSaturationThreshold() {
		return saturationThreshold;
	}

	public double getSlope() {
		return slope;
	}

	/**
	 * Returns the coordinates transform object used to transform screen
	 * coordinates to true 3D coordinates.
	 * 
	 * @return the coordinates transform object
	 */
	public CoordinatesTransform getTransform() {
		return transform;
	}

	public double getWeightThreshold() {
		return weightThreshold;
	}

	protected void notify(Image img, TargetStat stats, double[][] historyGrid) {
		Object[] listeners = this.listeners.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2)
			if (listeners[i] == TargetDebugListener.class)
				((TargetDebugListener) listeners[i + 1]).debug(this, img, stats, historyGrid);
	}

	protected void notifyFailure() {
		tries = 0;
		Object[] listeners = this.listeners.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2)
			if (listeners[i] == TargetListener.class)
				((TargetListener) listeners[i + 1]).targetFailure();
	}

	protected void notifyTarget(Point2D position, double weight) {
		tries = 0;
		Object[] listeners = this.listeners.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2)
			if (listeners[i] == TargetListener.class)
				((TargetListener) listeners[i + 1]).targetFound(position, weight);
	}

	/**
	 * Removes the specified debug listener from the listeners list.
	 * 
	 * @param listener
	 *            the listener to remove from the list
	 */
	public void removeDebugListener(TargetDebugListener listener) {
		listeners.remove(TargetDebugListener.class, listener);
	}

	/**
	 * Removes the specified target listener from the listeners list.
	 * 
	 * @param listener
	 *            the listener to remove from the list
	 */
	public void removeTargetListener(TargetListener listener) {
		listeners.remove(TargetListener.class, listener);
	}

	/**
	 * Defines the filtered color used by the filter.
	 * 
	 * @param filteredColor
	 *            the color to filter (hue in radians)
	 */
	public void setFilteredColor(double filteredColor) {
		this.filteredColor = filteredColor;
		clearHistory();
	}

	/**
	 * Defines the intensity threshold applied by the filter.
	 * 
	 * @param intensityThreshold
	 *            the intensity threshold applied by the filter
	 */
	public void setIntensityThreshold(double intensityThreshold) {
		this.intensityThreshold = intensityThreshold;
	}

	/**
	 * Defines the maximum number of tries to identify a target before notifying
	 * the failure.
	 * 
	 * @param maxTryCount
	 *            the maximum number of tries before failure notification
	 */
	public void setMaxTryCount(int maxTryCount) {
		this.maxTryCount = maxTryCount;
	}

	public void setNewDataRatio(double newDataRatio) {
		this.newDataRatio = newDataRatio;
	}

	public void setOldDataRatio(double oldDataRatio) {
		this.oldDataRatio = oldDataRatio;
	}

	/**
	 * Defines the saturation threshold applied by the filter.
	 * 
	 * @param saturationThreshold
	 *            the saturation threshold applied by the filter
	 */
	public void setSaturationThreshold(double saturationThreshold) {
		this.saturationThreshold = saturationThreshold;
	}

	public void setSlope(double slope) {
		this.slope = slope;
	}

	/**
	 * Defines the coordinates transform object used to transform screen
	 * coordinates to true 3D coordinates.
	 * 
	 * @param transform
	 *            the coordinates transform object
	 */
	public void setTransform(CoordinatesTransform transform) {
		this.transform = transform;
	}

	/**
	 * Defines the weight threshold applied by the filter.
	 * 
	 * @param weightThreshold
	 *            the weight threshold applied by the filter
	 */
	public void setWeightThreshold(double weightThreshold) {
		this.weightThreshold = weightThreshold;
	}

	private void store(TargetStat stats) {
		updateGrid(stats);
	}

	public void update() {
		Image img = acquisition.getImage();

		// Param�tres de filtrage
		ParameterBlock pb = new ParameterBlock();
		// Source
		pb.addSource(img);
		// Couleur filtr�e (radians)
		pb.add(filteredColor);
		// Seuil de saturation [0;1]
		pb.add(saturationThreshold);
		// Seuil d'intensit� (valeur) [0;1]
		pb.add(intensityThreshold);
		// Pente d�terminant la bande passante
		pb.add(slope);
		RenderedOp s = JAI.create("TargetFilter", pb, null);

		// Analyse spatiale de l'image
		TargetStat stats = (TargetStat) s.getProperty("Magnitude");
		// Ajout dans l'historique
		store(stats);
		// Analyse temporelle des donn�es
		analyze();

		// D�bogage
		notify(img, stats, historyGrid);
	}

	private void updateGrid(TargetStat stats) {
		// TODO utiliser des constantes
		double[][] d = stats.getData()[4];
		// Mise � jour de la grille historique
		for (int x = 0; x < historyGrid.length; x++)
			for (int y = 0; y < historyGrid[x].length; y++)
				// Diminution des poids existants
				historyGrid[x][y] = oldDataRatio * historyGrid[x][y] + newDataRatio * d[x][y];
	}
}
