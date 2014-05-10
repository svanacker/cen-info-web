package org.cen.cup.cup2011.robot.match;

import java.util.logging.Logger;

import org.cen.logging.LoggingUtils;
import org.cen.robot.brain.ConfigurationHandler;
import org.cen.robot.device.configuration.ConfigurationReadResult;
import org.cen.robot.match.MatchData;
import org.cen.robot.match.MatchSide;
import org.cen.robot.services.IRobotServiceProvider;
import org.cen.robot.utils.RobotUtils;

public class ConfigurationHandler2011 extends ConfigurationHandler {
	private static final Logger LOGGER = LoggingUtils.getClassLogger();

	public static final int SWITCH_MATCHSIDE = 7;

	public static final int SWITCH_HOMOLOGATION = 12;

	public static final int SWITCH_BONUS = 13;

	public static final int SWITCH_BUILD = 11;

	public ConfigurationHandler2011(IRobotServiceProvider servicesProvider) {
		super(servicesProvider);
	}

	@Override
	protected void updateConfiguration(ConfigurationReadResult result) {
		LOGGER.fine("updating configuration");

		MatchData2011 data = (MatchData2011) RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);
		data.setSide(result.getSwitch(SWITCH_MATCHSIDE) ? MatchSide.RED : MatchSide.VIOLET);
		LOGGER.fine("match side: " + data.getSide());

		data.setHomologation(result.getSwitch(SWITCH_HOMOLOGATION));
		LOGGER.fine("homologation: " + data.getHomologation());

		data.setBonus(result.getSwitch(SWITCH_BONUS));
		LOGGER.fine("bonus: " + data.getBonus());

		data.setBuild(result.getSwitch(SWITCH_BUILD));
		LOGGER.fine("build: " + data.getBuild());

		super.updateConfiguration(result);
	}
}
