package org.cen.cup.cup2009.robot.match;

import java.util.logging.Logger;

import org.cen.logging.LoggingUtils;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.RobotUtils;
import org.cen.robot.brain.ConfigurationHandler;
import org.cen.robot.device.configuration.ConfigurationReadResult;
import org.cen.robot.match.MatchData;
import org.cen.robot.match.MatchSide;

public class ConfigurationHandler2009 extends ConfigurationHandler {
	private static final Logger LOGGER = LoggingUtils.getClassLogger();

	private static final int SWITCH_MATCHSIDE = 6;

	private static final int MASK_HOMOLOGATIONCARD = 0x1E;

	private static final int HOMOLOGATION_BITS_COUNT = 4;

	public ConfigurationHandler2009(IRobotServiceProvider servicesProvider) {
		super(servicesProvider);
	}

	@Override
	protected void updateConfiguration(ConfigurationReadResult result) {
		LOGGER.fine("updating configuration");

		MatchData2009 data = (MatchData2009) RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);
		data.setSide(result.getSwitch(SWITCH_MATCHSIDE) ? MatchSide.RED : MatchSide.VIOLET);
		LOGGER.fine("match side: " + data.getSide());

		int homologation = (result.getValue() & MASK_HOMOLOGATIONCARD) >> 1;
		data.setHomologation(homologation != 0);
		int card = getCardValue(homologation);
		LOGGER.fine("homologation: " + data.getHomologation());

		if (data.getHomologation()) {
			data.setGameBoardConfiguration(card);
			LOGGER.fine("homologation card: " + data.getGameBoardConfiguration());
		}

		super.updateConfiguration(result);
	}

	private static int getCardValue(int value) {
		// Inversion des bits (les switchs sont plac�s en sens inverse)
		int v = 0;
		for (int i = 0; i < HOMOLOGATION_BITS_COUNT; i++) {
			if ((value & (1 << i)) != 0) {
				v |= 1 << ((HOMOLOGATION_BITS_COUNT - 1) - i);
			}
		}
		if (v < 1 || v > 10) {
			v = 0;
		}
		return v;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 16; i++) {
			System.out.println(getCardValue(i));
		}
	}
}
