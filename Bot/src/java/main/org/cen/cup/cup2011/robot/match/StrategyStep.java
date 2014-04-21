package org.cen.cup.cup2011.robot.match;

/**
 * Current step of the strategy process.
 * 
 * @author Emmanuel ZURMELY
 */
public enum StrategyStep {
	/**
	 * Starts moving
	 */
	START,
	/**
	 * Looks for a target.
	 */
	FIND_TARGET,
	/**
	 * Drops the pawn on a drop position.
	 */
	DROP_TARGET,
	/**
	 * Move the robot to analyze the gameboard.
	 */
	MOVE_TO_ANALYZE,
	/**
	 * Executes the sequence
	 */
	EXECUTE_SEQUENCE
}
