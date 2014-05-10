package org.cen.navigation;

import org.cen.geom.Point2D;
import java.util.Collection;
import java.util.Map;

public interface INavigationMap {

    /**
     * Ajoute le noeud spécifié.
     * 
     * @param l
     *            le noeud à ajouter
     */
    void addLocation(Location l);

    /**
     * Adds the path composed of the specified locations.
     * 
     * @param locations
     *            an array of locations
     */
    void addPath(String... locations);

    /**
     * Ajoute le chemin à la carte de navigation.
     * 
     * @param v
     *            le chemin à ajouter
     */
    void addVector(IPathVector v);

    /**
     * Ajoute un nouveau chemin reliant les noeuds spécifiés.
     * 
     * @param l1
     *            la position de départ
     * @param l2
     *            la position d'arrivée
     * @return le chemin ajouté à la carte
     */
    IPathVector addVector(Location l1, Location l2);

    void decayWeights(int value);

    /**
     * Renvoie la collection des noeuds.
     * 
     * @return la collection des noeuds
     */
    Collection<Location> getLocations();

    /**
     * Renvoie la table des noeuds.
     * 
     * @return la table des noeuds
     */
    Map<String, Location> getLocationsMap();

    Location getNearestLocation(Point2D coordinates);

    IPathVector getPathVector(Location l1, Location l2);

    /**
     * Renvoie la collection des chemins pouvant être parcourus.
     * 
     * @return la collection des chemins pouvant être parcourus
     */
    Collection<IPathVector> getPathVectors();

    /**
     * Resets the map.
     */
    void reset();

    void updateWeights(Point2D position, double radius, int additionalWeight, int maxWeight);
}