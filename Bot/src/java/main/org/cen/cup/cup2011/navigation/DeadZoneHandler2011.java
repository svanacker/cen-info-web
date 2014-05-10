package org.cen.cup.cup2011.navigation;

import org.cen.navigation.AbstractDeadZoneHandler;
import org.cen.robot.services.IRobotServiceProvider;

public class DeadZoneHandler2011 extends AbstractDeadZoneHandler {

	private static final int WEIGHT_DECAY = 400;

	private static final int COLLISION_WEIGHT = 800;

	private static final int COLLISION_RADIUS = 400;

	private static final int COLLISON_MAX_WEIGHT = 100000000;

	public DeadZoneHandler2011(IRobotServiceProvider servicesProvider) {
		super(servicesProvider, COLLISION_RADIUS, COLLISION_WEIGHT, COLLISON_MAX_WEIGHT, WEIGHT_DECAY);
	}
}
