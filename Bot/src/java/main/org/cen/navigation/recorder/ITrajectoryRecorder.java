package org.cen.navigation.recorder;

import java.util.List;

import org.cen.robot.services.IRobotService;

/**
 * Trajectory recording service.
 * 
 * @author Emmanuel ZURMELY
 */
public interface ITrajectoryRecorder extends IRobotService {

    /**
     * Clear the recorded data.
     */
    void clear();

    /**
     * Return the recorded trajectory.
     * 
     * @return the list of recorded trajectory
     */
    List<TrajectoryRecord> getRecords();

    /**
     * Returns true if the service is currently recording the trajectory.
     * 
     * @return true if recording is active, false otherwise
     */
    boolean isRecording();

    /**
     * Starts the trajectory recording.
     */
    void start();

    /**
     * Stops the trajectory recording.
     */
    void stop();
}
