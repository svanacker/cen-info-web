package org.cen.cup.cup2012.ui.web;

import org.cen.robot.match.MatchData;
import org.cen.robot.services.IRobotService;
import org.cen.robot.services.IRobotServiceProvider;
import org.cen.robot.utils.RobotUtils;

public class TargetsView2012 implements IRobotService {
	private IRobotServiceProvider servicesProvider;

	@Override
	public void setServicesProvider(IRobotServiceProvider provider) {
		this.servicesProvider = provider;
	}

	public String getGainData() {
		MatchData matchData = RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);
		String data = matchData.getProperty("gainData");
		return data;
	}
}
