package org.cen.cup.cup2012.robot.match;

import java.util.logging.Logger;

import org.cen.logging.LoggingUtils;
import org.cen.robot.brain.ConfigurationHandler;
import org.cen.robot.device.configuration.ConfigurationReadResult;
import org.cen.robot.match.MatchData;
import org.cen.robot.match.MatchSide;
import org.cen.robot.services.IRobotServiceProvider;
import org.cen.robot.utils.RobotUtils;

public class ConfigurationHandler2012 extends ConfigurationHandler {
	private static final Logger LOGGER = LoggingUtils.getClassLogger();

	public static final int SWITCH_MATCHSIDE = 7;

	private static final int SWITCH_HOMOLOGATION = 12;

	public ConfigurationHandler2012(IRobotServiceProvider servicesProvider) {
		super(servicesProvider);
	}

	@Override
	protected void updateConfiguration(ConfigurationReadResult result) {
		LOGGER.fine("updating configuration");

		MatchData2012 data = (MatchData2012) RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);
		data.setSide(result.getSwitch(SWITCH_MATCHSIDE) ? MatchSide.VIOLET : MatchSide.RED);
		LOGGER.fine("match side: " + data.getSide());

		data.setHomologation(result.getSwitch(SWITCH_HOMOLOGATION));
		LOGGER.fine("homologation: " + data.getHomologation());

		super.updateConfiguration(result);
	}
}
