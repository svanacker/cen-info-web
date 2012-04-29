package org.cen.simulRobot.brain.sonar;

import java.awt.Shape;

import org.cen.cup.cup2011.simulRobot.SonarArea;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.RobotUtils;
import org.cen.robot.device.DeviceRequestDispatcher;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.simulRobot.device.beacon.BeaconSimulReadRequest;
import org.cen.ui.gameboard.IGameBoardService;
import org.cen.ui.gameboard.ISimulGameBoard;
import org.cen.ui.gameboard.elements.ARobotAttributeElement;
import org.cen.ui.gameboard.elements.IMovableElement;
import org.cen.ui.gameboard.elements.SimulOpponentElement;
import org.cen.ui.gameboard.elements.SimulRobotElement;
import org.cen.ui.gameboard.elements.SimulSonarElement;

public class SonarSimulHandler extends Thread{
	private DeviceRequestDispatcher dispatcher;
	private ISimulGameBoard gameboard;
	private IMovableElement opponentElement;
	private long refreshRate;
	private ARobotAttributeElement sonarElement;
	private boolean terminated;


	public SonarSimulHandler(IRobotServiceProvider pServicesProvider){
		super(SonarSimulHandler.class.getSimpleName());
		dispatcher = pServicesProvider.getService(IRobotDevicesHandler.class).getRequestDispatcher();
		gameboard = (ISimulGameBoard)pServicesProvider.getService(IGameBoardService.class);
		opponentElement = gameboard.getElements(SimulOpponentElement.class).get(0);
		SimulRobotElement robotElement = gameboard.getElements(SimulRobotElement.class).get(0);
		SonarArea sonarArea = RobotUtils.getRobotAttribute(SonarArea.class, pServicesProvider);
		sonarElement = new SimulSonarElement(sonarArea, robotElement);
		robotElement.setRobotAttributeElement(sonarElement);
		terminated = false;
		refreshRate = 1000l;
	}

	private void checkOpponent(){

		Shape sonarShape = sonarElement.getAbsoluteBounds();

		if (sonarShape.intersects(opponentElement.getAbsoluteBounds().getBounds())){
			dispatcher.sendRequest(new BeaconSimulReadRequest((int)opponentElement.getPosition().getX(), (int)opponentElement.getPosition().getY()));
		}
	}

	@Override
	public void run() {
		while(!terminated){
			try {
				checkOpponent();
				Thread.sleep(refreshRate);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void shutdown(){
		terminated = true;
	}
}
