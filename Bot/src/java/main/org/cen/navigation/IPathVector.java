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
    boolean containsLocation(Location l);

    /**
     * Renvoie le coût du parcourt de ce chemin.
     * 
     * @return le coût de parcourt de ce chemin
     */
    double getCost();

    /**
     * Renvoie la position d'arrivée
     * 
     * @return la position d'arrivée
     */
    Location getEnd();

    /**
     * Returns the start and end locations as a collection
     * 
     * @return the start and end locations as a collection
     */
    Collection<Location> getLocations();

    Location getOtherEnd(Location l);

    /**
     * Renvoie la position de départ
     * 
     * @return la position de départ
     */
    Location getStart();

    /**
     * Returns the graphical interface for drawing the path onto the gameboard
     * view.
     * 
     * @param name
     *            the name of the returned element
     * @return the graphical interface for drawing the trajectory
     */
    IGameBoardElement getGameBoardElement(String name);
}