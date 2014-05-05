package org.cen.robot.match;

import java.util.Properties;

import org.cen.math.PropertiesMathUtils;
import org.cen.robot.AdvancedRobotAttribute;
import org.cen.robot.match.strategy.IGameStrategy;

/**
 * Informations and properties about a match.
 * 
 * @author svanacker
 * @version 12/03/2007
 */
public class MatchData extends AdvancedRobotAttribute {

	private static final int DEFAULT_DURATION = 90000;

	private static final String PROPERTY_DURATION = "duration";

	private static final String PROPERTY_HOMOLOGATION = "homologation";

	private static final String PROPERTY_SIDE = "side";

	private static final String PROPERTY_STRATEGY = "strategy";

	private static final String PROPERTY_STARTTIME = "startTime";

	/**
	 * Constructor.
	 */
	public MatchData() {
		super();
		setSide(MatchSide.VIOLET);
	}

	/**
	 * Returns the match duration in milliseconds.
	 * 
	 * @return the match duration in milliseconds
	 */
	public long getDuration() {
		return (Long) get(PROPERTY_DURATION);
	}

	/**
	 * Returns true when the match is a homologation match, false otherwise.
	 * 
	 * @return true for a homologation match, false otherwise
	 */
	public boolean getHomologation() {
		Boolean value = (Boolean) get(PROPERTY_HOMOLOGATION);
		if (value == null) {
			return false;
		}
		return value;
	}

	/**
	 * Returns the player side of the match.
	 * 
	 * @return the side of the match
	 */
	public MatchSide getSide() {
		return (MatchSide) get(PROPERTY_SIDE);
	}

	/**
	 * Returns the start time of the match. The start time is the time used for
	 * computing the elapsed match time.
	 * 
	 * @return the system time of the match start
	 */
	public long getStartTime() {
		return (Long) get(PROPERTY_STARTTIME);
	}

	/**
	 * Returns the active strategy.
	 * 
	 * @return the active strategy
	 */
	public IGameStrategy getStrategy() {
		return (IGameStrategy) get(PROPERTY_STRATEGY);
	}

	/**
	 * Returns the time since the startup of the match.
	 * 
	 * @return the time since the startup of the match
	 */
	public long getTimeSinceStartup() {
		return System.currentTimeMillis() - getStartTime();
	}

	/**
	 * Returns true if the match is finished. The match is finished when the
	 * elapsed match time is greater than the match duration.
	 * 
	 * @return true if the match is finished, false otherwise
	 */
	public boolean isFinished() {
		return (getTimeSinceStartup() > getDuration());
	}

	/**
	 * Returns true if the match has been started.
	 * 
	 * @return true if the match has been started
	 */
	public boolean isStarted() {
		return (getStartTime() > 0);
	}

	/**
	 * Sets the current strategy.
	 * 
	 * @param strategy
	 *            the current strategy
	 */
	public void setStrategy(IGameStrategy strategy) {
		put(PROPERTY_STRATEGY, strategy);
	}

	/**
	 * Sets the duration of the match.
	 * 
	 * @param duration
	 *            the duration of the match in milliseconds
	 */
	public void setDuration(long duration) {
		put(PROPERTY_DURATION, duration);
	}

	/**
	 * Sets the match properties from a properties object.
	 * 
	 * @param properties
	 *            the properties object
	 * @param prefix
	 *            the prefix of the properties to set
	 */
	public void setFromProperties(Properties properties, String prefix) {
		double duration = PropertiesMathUtils.getDouble(properties, prefix
				+ PROPERTY_DURATION, DEFAULT_DURATION);
		// convert to milliseconds
		setDuration((long) (duration * 1000));
	}

	/**
	 * Sets the homologation flag.
	 * 
	 * @param value
	 *            true for a homologation match, false otherwise
	 */
	public void setHomologation(boolean value) {
		put(PROPERTY_HOMOLOGATION, Boolean.valueOf(value));
	}

	/**
	 * Sets the match side.
	 * 
	 * @param side
	 *            the match side
	 */
	public void setSide(MatchSide side) {
		put(PROPERTY_SIDE, side);
	}

	/**
	 * Starts the match.
	 * 
	 * @throws Exception
	 */
	public void start() throws Exception {
		if (isStarted()) {
			throw new Exception("The match is already started !");
		}
		put(PROPERTY_STARTTIME, System.currentTimeMillis());
	}
}
