package org.cen.robot.graphics;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * Utils methods to convert coordinates
 * 
 * @author Stephane
 * @version 04/03/2007
 */
@Deprecated
public class GameBoardPanelUtils {
	/**
	 * Returns the real Point in Panel for a point defined in pixels
	 * 
	 * @param x
	 *            the coordinates defined for the panel
	 * @param y
	 *            the coordinates defined for the panel
	 * @return the point defined as coordinates
	 */
	public static Point2D.Double toRealPoint(int x, int y, double panelHeight, double scale) {
		Point2D.Double result = new Point2D.Double(x, y);
		result.y = panelHeight - y;

		result.x /= scale;
		result.y /= scale;

		return result;
	}

	/**
	 * Returns the point in Panel for a point defined in mm
	 * 
	 * @param x
	 *            the real coordinates in x for the point in mm
	 * @param y
	 *            the real coordinates in y for the point in mm
	 * @return the point defined as coordinates in panel
	 */
	public static Point toPanelPoint(Point2D.Double point, double yLength, double scale) {
		Point result = new Point();
		point.y = yLength - point.y;

		result.x = (int) (point.x * scale);
		result.y = (int) (point.y * scale);

		return result;
	}

}
