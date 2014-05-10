package org.cen.cup.cup2010.robot.match;

import java.util.logging.Logger;

import org.cen.logging.LoggingUtils;
import org.cen.robot.brain.ConfigurationHandler;
import org.cen.robot.device.configuration.ConfigurationReadResult;
import org.cen.robot.match.MatchData;
import org.cen.robot.match.MatchSide;
import org.cen.robot.services.IRobotServiceProvider;
import org.cen.robot.utils.RobotUtils;

public class ConfigurationHandler2010 extends ConfigurationHandler {
	private static final Logger LOGGER = LoggingUtils.getClassLogger();

	public static final int SWITCH_MATCHSIDE = 7;

	public ConfigurationHandler2010(IRobotServiceProvider servicesProvider) {
		super(servicesProvider);
	}

	@Override
	protected void updateConfiguration(ConfigurationReadResult result) {
		LOGGER.fine("updating configuration");

		MatchData2010 data = (MatchData2010) RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);
		data.setSide(result.getSwitch(SWITCH_MATCHSIDE) ? MatchSide.RED : MatchSide.VIOLET);
		LOGGER.fine("match side: " + data.getSide());

		super.updateConfiguration(result);
	}
}
