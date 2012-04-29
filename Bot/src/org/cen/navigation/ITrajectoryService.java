package org.cen.navigation;

import java.awt.geom.Point2D;
import java.util.List;

import org.cen.robot.IRobotService;
import org.cen.robot.device.RobotDeviceRequest;
import org.cen.util.Holder;
import org.springframework.beans.factory.annotation.Required;

/**
 * Interface of the trajectory service. The trajectory service is use to compute
 * paths using a navigation map.
 * 
 * @author Emmanuel ZURMELY
 */
public interface ITrajectoryService extends IRobotService {
	/**
	 * Builds the navigation requests corresponding to a given path.
	 * 
	 * @param path
	 *            the path to follow
	 * @param orientation
	 *            the initial orientation angle of the robot in radians
	 * @param requests
	 *            the list of requests
	 * @param backward
	 *            flag indicating that the robot moves backward
	 * @return the final orientation angle of the robot in radians
	 */
	public abstract double buildTrajectoryRequests(List<Point2D> path, double orientation, List<RobotDeviceRequest> requests, boolean backward);

	/**
	 * Returns the navigation map.
	 * 
	 * @return the navigation map
	 */
	public abstract INavigationMap getNavigationMap();

	/**
	 * Returns a list containing the successive positions forming the shorter
	 * path between the given positions.
	 * 
	 * @param start
	 *            the start point
	 * @param end
	 *            the end point
	 * @return a list of request for navigating from start point to end point
	 */
	public abstract List<Location> getPath(Location start, Location end);

	/**
	 * Returns a list containing the successive positions forming the shorter
	 * path between the given positions.
	 * 
	 * @param start
	 *            the start point coordinates
	 * @param end
	 *            the end point coordinates
	 * @param cost
	 *            the cost of the returned path
	 * @return a list of request for navigating from start point to end point
	 */
	public abstract List<Location> getPath(Location start, Location end, Holder<Integer> cost);

	/**
	 * Returns a list containing the successive positions forming the shorter
	 * path between the given positions.
	 * 
	 * @param start
	 *            the start point coordinates
	 * @param end
	 *            the end point coordinates
	 * @return a list of request for navigating from start point to end point
	 */
	public abstract List<Location> getPath(Point2D origin, Point2D destination);

	/**
	 * Returns the list of the locations' coordinates.
	 * 
	 * @param currentPath
	 *            a list of locations
	 * @return the list of the locations' coordinates
	 */
	public abstract List<Point2D> getPathFromLocations(List<Location> currentPath);

	/**
	 * @deprecated. Superseded by buildTrajectoryRequests.
	 * @see buildTrajectoryRequests
	 */
	@Deprecated
	public abstract List<RobotDeviceRequest> getRequests(List<Location> path);

	/**
	 * Returns the rotation angle threshold, indicating the minimum angle needed
	 * to issue a rotation request.
	 * 
	 * @return the rotation angle threshold
	 */
	public abstract double getRotationAngleThreshold();

	/**
	 * Sets the navigation map.
	 * 
	 * @param map
	 *            the navigation map
	 */
	@Required
	public abstract void setNavigationMap(INavigationMap map);

	/**
	 * Sets the rotation angle threshold, indicating the minimum angle needed to
	 * issue a rotation request.
	 * 
	 * @param rotationAngleThreshold
	 *            the rotation angle threshold
	 */
	public abstract void setRotationAngleThreshold(double rotationAngleThreshold);
}
