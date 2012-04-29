package org.cen.navigation;

import java.util.Collection;

import org.cen.ui.gameboard.IGameBoardElement;

public interface IPathVector {

	/**
	 * Détermine si l'une des extrémités est la position spécifiée.
	 * 
	 * @param l
	 *            la position à tester
	 * @return true si l'un des noeuds est la position spécifiée, false sinon
	 */
	public abstract boolean containsLocation(Location l);

	/**
	 * Renvoie le coût du parcourt de ce chemin.
	 * 
	 * @return le coût de parcourt de ce chemin
	 */
	public abstract double getCost();

	/**
	 * Renvoie la position d'arrivée
	 * 
	 * @return la position d'arrivée
	 */
	public abstract Location getEnd();

	/**
	 * Returns the start and end locations as a collection
	 * 
	 * @return the start and end locations as a collection
	 */
	public abstract Collection<Location> getLocations();

	public abstract Location getOtherEnd(Location l);

	/**
	 * Renvoie la position de départ
	 * 
	 * @return la position de départ
	 */
	public abstract Location getStart();

	/**
	 * Returns the graphical interface for drawing the path onto the gameboard
	 * view.
	 * 
	 * @param name
	 *            the name of the returned element
	 * @return the graphical interface for drawing the trajectory
	 */
	public abstract IGameBoardElement getGameBoardElement(String name);
}