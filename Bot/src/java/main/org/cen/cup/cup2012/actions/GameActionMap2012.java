package org.cen.cup.cup2012.actions;

import org.cen.actions.AbstractGameActionMap;
import org.cen.robot.services.IRobotServiceProvider;

public class GameActionMap2012 extends AbstractGameActionMap {

	private final IRobotServiceProvider servicesProvider;

	public GameActionMap2012(IRobotServiceProvider servicesProvider) {
		super();
		this.servicesProvider = servicesProvider;
	}
}
