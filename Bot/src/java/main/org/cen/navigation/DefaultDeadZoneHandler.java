package org.cen.navigation;

import org.cen.robot.IRobotServiceProvider;

public class DefaultDeadZoneHandler extends AbstractDeadZoneHandler {

    private static final int WEIGHT_DECAY = 200;

    private static final int COLLISION_WEIGHT = 800;

    private static final int COLLISION_RADIUS = 400;

    private static final int COLLISION_MAX_WEIGHT = 2400;

    public DefaultDeadZoneHandler(IRobotServiceProvider servicesProvider) {
        super(servicesProvider, COLLISION_RADIUS, COLLISION_WEIGHT, COLLISION_MAX_WEIGHT, WEIGHT_DECAY);
    }
}
