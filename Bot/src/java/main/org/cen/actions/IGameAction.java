package org.cen.actions;

import org.cen.geom.Point2D;

/**
 * Interface describing an action available somewhere on the gameboard.
 * 
 * @author Emmanuel ZURMELY
 */
public interface IGameAction {

    /**
     * Returns the position on the gameboard where the action can be handled by
     * using the given action handler. The returned value must be coherent with
     * isEnabled.
     * 
     * @param handler
     *            the action handler
     * @param pathElement
     *            the trajectory path element
     * @return the position on the gameboard from where the action can be
     *         handled by the given handler, or NULL if the action can not be
     *         handled by the given handler
     */
    Point2D getActingPosition(IGameActionHandler handler, TrajectoryPathElement pathElement);

    /**
     * Returns a description of this action
     * 
     * @return a description of this action
     */
    String getDescription();

    /**
     * Returns the priority of this game action.
     * 
     * @return the priority of this game action
     */
    int getPriority();

    /**
     * Determines if the action is enabled for the given handler. The returned
     * value must be coherent with getActingPosition.
     * 
     * @param handler
     *            the action handler
     * @return TRUE if the action is enabled, FALSE otherwise
     */
    boolean isEnabled(IGameActionHandler handler);

    /**
     * Returns a flag indicating whether the robot must be stopped when handling
     * this action.
     * 
     * @param handler
     *            the action handler
     * @return TRUE if the robot must be stop to handle this action, FALSE if it
     *         can handle this action while running
     */
    boolean mustStop(IGameActionHandler handler);
}
