package org.cen.cup.cup2011.robot.match;

import org.cen.geom.Point2D;
import java.util.Comparator;

import org.cen.cup.cup2011.gameboard.configuration.PawnPosition;

/**
 * Comparator used to sort the pawns according to their distance from a given
 * position.
 * 
 * @author Emmanuel ZURMELY
 */
public class PawnDistanceComparator implements Comparator<PawnPosition> {
	Point2D position;

	/**
	 * Constructor
	 * 
	 * @param position
	 *            the reference position
	 */
	public PawnDistanceComparator(Point2D position) {
		super();
		this.position = position;
	}

	@Override
	public int compare(PawnPosition o1, PawnPosition o2) {
		Point2D p1 = o1.getPosition();
		Point2D p2 = o2.getPosition();
		double d1 = p1.distance(position);
		double d2 = p2.distance(position);
		return Double.compare(d1, d2);
	}
}
