package org.cen.ui.web;

import org.cen.com.ClientTCPManager;
import org.cen.robot.IRobotServiceProvider;
import org.cen.simulRobot.RobotSwitches;
import org.cen.simulRobot.match.ISimulMatchStrategy;
import org.cen.ui.gameboard.ISimulGameBoard;

public class ParametersView {
	private long beaconRefreshRate;
	private ClientTCPManager comService;
	private int configuration;		//position des interrupteurs
	private ISimulGameBoard gameboard;
	private String host;			//adresse du serveur sous la forme http://nom de domaine (ou IP)
	private ISimulMatchStrategy matchStrategie;
	private double opponentSpeed;
	IRobotServiceProvider provider;
	private int readSpeed;		//vitesse de lecture (*1 = normal)
	//private IRobot robot;
	private RobotSwitches robotInterrupteur;
	private double robotRefreshRate;
	private double robotSpeed;


	public long getBeaconRefreshRate() {
		return beaconRefreshRate;
	}

	public int getConfiguration() {
		return configuration;
	}

	public String getHost() {
		return host;
	}

	public double getOpponentSpeed() {
		return opponentSpeed;
	}

	public int getReadSpeed() {
		return readSpeed;
	}

	public double getRobotRefreshRate() {
		return robotRefreshRate;
	}

	public double getRobotSpeed() {
		return robotSpeed;
	}

	public void sendData(){
		//TODO Put all data creations in setData method

		//robotInterrupteur.setInterrupteurs(configuration);
		//gameboard.getMovingHandler().setReadSpeed(readSpeed);
		//matchStrategie.getBeaconHandler().setRefreshRate(beaconRefreshRate);
		//comService.getProperties().setProperty(ClientApplicationConst.PROPERTY_TCPHOST, host);

		System.out.println("ParametersData Sended");
	}

	public void setBeaconRefreshRate(long opponentRefreshRate) {
		this.beaconRefreshRate = opponentRefreshRate;
	}

	public void setConfiguration(int configuration) {
		this.configuration = configuration;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setOpponentSpeed(double opponentSpeed) {
		this.opponentSpeed = opponentSpeed;
	}

	public void setReadSpeed(int readSpeed) {
		this.readSpeed = readSpeed;
	}

	public void setRobotRefreshRate(double robotRefreshRate) {
		this.robotRefreshRate = robotRefreshRate;
	}

	public void setRobotSpeed(double robotSpeed) {
		this.robotSpeed = robotSpeed;
	}

	public void setServicesProvider(IRobotServiceProvider provider) {
		//		this.provider = provider;
		//		IRobot robot = RobotUtils.getRobot(provider);
		//		this.robotInterrupteur = robot.getAttribute(RobotSwitches.class);
		//		this.matchStrategie = provider.getService(ISimulMatchStrategy.class);
		//		this.comService = (ClientTCPManager)provider.getService(IComService.class);
		//		this.gameboard = provider.getService(ISimulGameBoard.class);

		//get the default values
		//configuration = robotInterrupteur.getSwitches();
		//readSpeed = gameboard.getMovingHandler().getReadSpeed();
		//beaconRefreshRate = matchStrategie.getBeaconHandler().getRefreshRate();
		//host = comService.getProperties().getProperty(ClientApplicationConst.PROPERTY_TCPHOST);
	}


}
