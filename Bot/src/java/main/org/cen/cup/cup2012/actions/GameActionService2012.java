package org.cen.cup.cup2012.actions;

import java.util.List;

import org.cen.actions.AbstractGameActionService;
import org.cen.actions.IGameAction;
import org.cen.actions.IGameActionHandler;
import org.cen.geom.Point2D;
import org.cen.robot.device.request.IRobotDeviceRequest;
import org.cen.robot.services.IRobotServiceProvider;

public class GameActionService2012 extends AbstractGameActionService {

    public GameActionService2012(IRobotServiceProvider servicesProvider) {
        super(servicesProvider);
    }

    @Override
    protected void handleAction(List<IRobotDeviceRequest> requests, Point2D position, IGameAction action,
            IGameActionHandler handler) {

    }

    @Override
    protected double handleMove(List<IRobotDeviceRequest> requests, Point2D start, Point2D end, double orientation) {
        return 0;
    }

}
