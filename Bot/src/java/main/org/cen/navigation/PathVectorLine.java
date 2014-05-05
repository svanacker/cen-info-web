package org.cen.navigation;

import org.cen.ui.gameboard.IGameBoardElement;

/**
 * Représente un chemin entre deux noeuds.
 * 
 * @author Emmanuel ZURMELY
 */
public class PathVectorLine extends AbstractPathVector {

    protected int weight = 0;

    protected int type;

    public static final int VERTICAL = 0;

    public static final int DIAGONAL1 = 1;

    public static final int HORIZONTAL = 2;

    public static final int DIAGONAL2 = 3;

    /**
     * Constructeur.
     * 
     * @param l1
     *            position de départ
     * @param l2
     *            position d'arrivée
     */
    public PathVectorLine(Location l1, Location l2) {
        this(l1, l2, 0);
    }

    /**
     * Constructeur.
     * 
     * @param l1
     *            position de départ
     * @param l2
     *            position d'arrivée
     * @param weight
     *            poids associé au parcourt du chemin
     */
    public PathVectorLine(Location l1, Location l2, int weight) {
        super(l1, l2);
        this.weight = weight;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.cen.navigation.IPathVector2#getCost()
     */
    @Override
    public double getCost() {
        return l1.getDistance(l2) + weight;
    }

    /**
     * Renvoie le type de chemin.
     * 
     * @return le type du chemin
     */
    public int getType() {
        return type;
    }

    /**
     * Renvoie le poids du chemin.
     * 
     * @return le poids du chemin
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Définit le type du type chemin.
     * 
     * @param type
     *            le type du type chemin
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Définit le poids du chemin.
     * 
     * @param weight
     *            le poids à associer au chemin
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public IGameBoardElement getGameBoardElement(String name) {
        return null;
    }
}
