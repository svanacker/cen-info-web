package org.cen.simulRobot.brain.beacon;


import java.awt.geom.Point2D;
import java.util.List;

import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.device.DeviceRequestDispatcher;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.simulRobot.device.beacon.BeaconSimulReadRequest;
import org.cen.ui.gameboard.IGameBoardService;
import org.cen.ui.gameboard.ISimulGameBoard;
import org.cen.ui.gameboard.elements.SimulOpponentElement;

/**
 * Calcule et envoie la position absolue de l'adversaire
 * 
 * @author Benouamer Omar
 *
 */
public class BeaconSimulHandler extends Thread{
	private DeviceRequestDispatcher dispatcher;
	private Long refreshRate;

	private IRobotServiceProvider servicesProvider;
	private boolean terminated;


	public BeaconSimulHandler(IRobotServiceProvider aservicesProvider){
		terminated = false;
		dispatcher = aservicesProvider.getService(IRobotDevicesHandler.class).getRequestDispatcher();
		this.servicesProvider = aservicesProvider;
		refreshRate = 1000l;
	}

	public long getRefreshRate() {
		return refreshRate;
	}

	@Override
	public void run(){
		Point2D opponentPosition;

		// recupére la reference sur l'opponent
		ISimulGameBoard gameBoard = (ISimulGameBoard)servicesProvider.getService(IGameBoardService.class);

		List<SimulOpponentElement> opponent = gameBoard.getElements(SimulOpponentElement.class);

		while(!terminated){

			opponentPosition = opponent.get(0).getPosition();
			dispatcher.sendRequest(new BeaconSimulReadRequest((int)opponentPosition.getX(), (int)opponentPosition.getY()));


			try {
				Thread.sleep(refreshRate);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	public void setRefreshRate(long refreshRate) {
		this.refreshRate = refreshRate;
	}

	public void shutdown(){
		terminated = true;
	}
}
