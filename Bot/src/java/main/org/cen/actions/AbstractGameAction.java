package org.cen.actions;

import org.cen.geom.Point2D;

public abstract class AbstractGameAction implements IGameAction {
	
	/**
	 * Returns the absolute orientation of the trajectory path
	 * 
	 * @param path
	 *            the trajectory path
	 * @return the absolute orientation of the trajectory path
	 */
	public double getPathOrientation(TrajectoryPathElement path) {
		Point2D s = path.getStart();
		Point2D e = path.getEnd();
		double sx = s.getX();
		double sy = s.getY();
		double ex = e.getX();
		double ey = e.getY();
		return Math.atan2(ey - sy, ex - sx);
	}

	/**
	 * Returns the absolute position that the robot must reach to have the
	 * action handler at the position of the trajectory's end.
	 * 
	 * @param path
	 *            the trajectory path
	 * @param robotPosition
	 *            the position of the handler relative to the central point of
	 *            the robot
	 * @return the absolute position that the robot must reach
	 */
	public Point2D getPositionFromPathEnd(TrajectoryPathElement path, Point2D robotPosition) {
		Point2D result = new Point2D.Double();
		// Distance du dispositif du point central du robot
		double d = result.distance(robotPosition);
		// Position angulaire du dispositif par rapport à l'axe des roues
		double angle = Math.atan2(robotPosition.getY(), robotPosition.getX());
		// Distinction avant / arrière
		if (angle < 0 || angle > Math.PI) {
			d = -d;
		}
		// Angle absolu du dispositif par rapport au point central
		double a = getPathOrientation(path) + angle - (Math.PI / 2d);
		Point2D e = path.getEnd();
		// Position absolue du dispositif sur la table
		result.setLocation(e.getX() - d * Math.cos(a), e.getY() - d * Math.sin(a));
		return result;
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public boolean isEnabled(IGameActionHandler handler) {
		return true;
	}

	@Override
	public boolean mustStop(IGameActionHandler handler) {
		return false;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[description=" + getDescription() + "]";
	}
}
