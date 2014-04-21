package org.cen.navigation;

import java.util.List;

import org.cen.robot.IRobotService;

/**
 * Trajectory recording service.
 * 
 * @author Emmanuel ZURMELY
 */
public interface ITrajectoryRecorder extends IRobotService {
	/**
	 * Clear the recorded data.
	 */
	public void clear();

	/**
	 * Return the recorded trajectory.
	 * 
	 * @return the list of recorded trajectory
	 */
	public List<TrajectoryRecord> getRecords();

	/**
	 * Returns true if the service is currently recording the trajectory.
	 * 
	 * @return true if recording is active, false otherwise
	 */
	public boolean isRecording();

	/**
	 * Starts the trajectory recording.
	 */
	public void start();

	/**
	 * Stops the trajectory recording.
	 */
	public void stop();
}
