package org.cen.navigation.recorder;

import org.cen.geom.Point2D;
import java.util.Date;
import java.util.List;

/**
 * Class representing a recorded trajectory.
 * 
 * @author Emmanuel ZURMELY
 */
public class TrajectoryRecord {
	private boolean active;

	private Date endDate;

	private Date startDate;

	private List<Point2D> trajectory;

	/**
	 * Constructor.
	 */
	public TrajectoryRecord() {
		super();
		startDate = new Date();
	}

	/**
	 * Returns the trajectory end date.
	 * 
	 * @return the end date
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Returns the duration of the trajectory.
	 * 
	 * @return the duration of the trajectory in second.
	 */
	public double getLength() {
		return (endDate.getTime() - startDate.getTime()) / 1000d;
	}

	/**
	 * Returns the trajectory start date.
	 * 
	 * @return the start date
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Returns the coordinates of the points decribing this trajectory.
	 * 
	 * @return a list of the points describing this trajectory
	 */
	public List<Point2D> getTrajectory() {
		return trajectory;
	}

	/**
	 * Determines whether this trajectory is active.
	 * 
	 * @return a boolean value indicating whether the trajectory is active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets the active status of this trajectory.
	 * 
	 * @param active
	 *            the boolean value specifying whether the trajectory is active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Sets the end date of this record.
	 * 
	 * @param endDate
	 *            the end date
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * Sets the coordinates of the trajectory.
	 * 
	 * @param trajectory
	 *            a list of the points corresponding to the trajectory
	 */
	public void setTrajectory(List<Point2D> trajectory) {
		this.trajectory = trajectory;
	}
}