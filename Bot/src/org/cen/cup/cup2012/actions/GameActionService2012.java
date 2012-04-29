package org.cen.cup.cup2012.actions;

import java.awt.geom.Point2D;
import java.util.List;

import org.cen.actions.AbstractGameActionService;
import org.cen.actions.IGameAction;
import org.cen.actions.IGameActionHandler;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.device.RobotDeviceRequest;

public class GameActionService2012 extends AbstractGameActionService {

	public GameActionService2012(IRobotServiceProvider servicesProvider) {
		super(servicesProvider);
	}

	@Override
	protected void handleAction(List<RobotDeviceRequest> requests, Point2D position, IGameAction action,
			IGameActionHandler handler) {

	}

	@Override
	protected double handleMove(List<RobotDeviceRequest> requests, Point2D start, Point2D end, double orientation) {
		return 0;
	}

}
