package org.cen.ui.gameboard.elements;

import java.awt.Shape;
import java.awt.geom.Point2D;

/**
 * Interface for a graphical representation of a path on a trajectory.
 * 
 * @author Emmanuel ZURMELY
 */
public interface ITrajectoryPath {
	/**
	 * Returns the orientation of the robot when ending the trajectory.
	 * 
	 * @return the orientation of the robot when ending the trajectory in
	 *         radians
	 */
	public double getRobotFinalAngle();

	/**
	 * Returns the orientation of the robot when starting the trajectory.
	 * 
	 * @return the orientation of the robot when starting the trajectory in
	 *         radians
	 */
	public double getRobotInitialAngle();

	/**
	 * Returns the path object representing the trajectory.
	 * 
	 * @return the path object representing the trajectory
	 */
	public Shape getPath();

	/**
	 * Returns the position of the element on the gameboard.
	 * 
	 * @return the position of the element on the gameboard
	 */
	public Point2D getPosition();

	/**
	 * Returns the control points of the trajectory if any.
	 * 
	 * @return the control points of the trajectory if any
	 */
	public Point2D[] getTrajectoryControlPoints();

	/**
	 * Returns the ending position of the trajectory.
	 * 
	 * @return the ending position of the trajectory
	 */
	public Point2D getTrajectoryEnd();

	/**
	 * Returns the starting position of the trajectory.
	 * 
	 * @return the starting position of the trajectory
	 */
	public Point2D getTrajectoryStart();
}
