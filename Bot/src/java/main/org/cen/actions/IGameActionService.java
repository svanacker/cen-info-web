package org.cen.actions;

import java.util.Collection;
import java.util.List;

import org.cen.robot.device.request.IRobotDeviceRequest;
import org.cen.robot.services.IRobotService;

/**
 * Interface of the service that handles the game board actions.
 * 
 * @author Emmanuel ZURMELY
 */
public interface IGameActionService extends IRobotService {

    /**
     * Builds the requests list handling the gameboard actions available at the
     * given locations.
     * 
     * @param data
     *            the trajectory data
     * @param map
     *            the map of the actions on the gameboard
     * @return the requests list, NULL if no more requests can be built
     */
    List<IRobotDeviceRequest> buildNextRequests(TrajectoryData data, IGameActionMap map);

    /**
     * Returns the action handlers of the robot.
     * 
     * @return the action handlers of the robot
     */
    Collection<IGameActionHandler> getRobotActionHandlers();
}
