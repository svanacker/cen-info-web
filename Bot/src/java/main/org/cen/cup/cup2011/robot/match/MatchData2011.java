package org.cen.cup.cup2011.robot.match;

import org.cen.robot.match.MatchData;

/**
 * Specific match data for the cup 2011.
 * 
 * @author Emmanuel ZURMELY
 */
public class MatchData2011 extends MatchData {
	private static final String PROPERTY_BUILD = "build";

	private static final String PROPERTY_BONUS = "bonus";

	public boolean getBonus() {
		return (Boolean) get(PROPERTY_BONUS);
	}

	public boolean getBuild() {
		return (Boolean) get(PROPERTY_BUILD);
	}

	public void setBonus(boolean value) {
		put(PROPERTY_BONUS, value);
	}

	public void setBuild(boolean value) {
		put(PROPERTY_BUILD, value);
	}
}
