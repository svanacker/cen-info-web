package org.cen.cup.cup2009.robot.match;

import org.cen.robot.match.MatchData;

public class MatchData2009 extends MatchData {
	private static final String PROPERTY_GAMEBOARDCONFIGURATION = "gameBoardConfiguration";

	public static final String PROPERTY_TRAJECTORY_SUFFIX = "trajectorySuffix";

	private static final long serialVersionUID = 1L;

	public int getGameBoardConfiguration() {
		Integer value = (Integer) get(PROPERTY_GAMEBOARDCONFIGURATION);
		if (value != null) {
			return value;
		} else {
			return 0;
		}
	}

	public void setGameBoardConfiguration(int id) {
		put(PROPERTY_GAMEBOARDCONFIGURATION, id);
	}

	public void setTrajectorySuffix(String suffix) {
		put(PROPERTY_TRAJECTORY_SUFFIX, suffix);
	}
}
