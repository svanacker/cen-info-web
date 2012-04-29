package org.cen.cup.cup2011.robot.match;

import java.awt.geom.Point2D;

import org.cen.cup.cup2011.device.vision2011.LookForPawnRequest;
import org.cen.cup.cup2011.device.vision2011.PawnPositionResult;
import org.cen.cup.cup2011.device.vision2011.Vision2011Device;
import org.cen.cup.cup2011.gameboard.configuration.GameboardConfigurationHandler2011;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.brain.AbstractDeviceHandler;
import org.cen.robot.device.RobotDeviceRequest;
import org.cen.robot.device.RobotDeviceResult;
import org.cen.robot.match.IMatchStrategyHandler;

public class Vision2011Handler extends AbstractDeviceHandler {
	private GameboardConfigurationHandler2011 gameBoardConfigurationHandler;

	private IMatchStrategyHandler strategyHandler;

	public Vision2011Handler(IRobotServiceProvider servicesProvider, GameboardConfigurationHandler2011 gameBoardConfigurationHandler, IMatchStrategyHandler strategyHandler) {
		super(servicesProvider);
		this.gameBoardConfigurationHandler = gameBoardConfigurationHandler;
		this.strategyHandler = strategyHandler;
	}

	@Override
	public String getDeviceName() {
		return Vision2011Device.NAME;
	}

	@Override
	public void handleResult(RobotDeviceResult result) {
		if (result instanceof PawnPositionResult) {
			PawnPositionResult r = (PawnPositionResult) result;
			if (r.isTerminated()) {
				setAnalysisTerminated();
			} else {
				updatePawnPosition(r.getPoint());
			}
		}
	}

	private void setAnalysisTerminated() {
		gameBoardConfigurationHandler.setAnalysisTerminated();
		strategyHandler.handleEvent(new GameboardAnalysisDoneEvent());
	}

	private void updatePawnPosition(Point2D point) {
		gameBoardConfigurationHandler.updatePawnPosition(point);
	}
}
