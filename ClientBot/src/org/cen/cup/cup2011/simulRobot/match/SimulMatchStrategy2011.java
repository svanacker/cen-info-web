package org.cen.cup.cup2011.simulRobot.match;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.cen.com.IComService;
import org.cen.cup.cup2011.simulGameboard.elements.SimulPawnElement;
import org.cen.robot.IRobotFactory;
import org.cen.robot.IRobotService;
import org.cen.robot.IRobotServiceInitializable;
import org.cen.robot.match.IMatchEvent;
import org.cen.robot.match.events.MatchFinishedEvent;
import org.cen.robot.match.events.MatchStartedEvent;
import org.cen.robot.match.events.RobotInitializedEvent;
import org.cen.simulRobot.brain.beacon.BeaconSimulHandler;
import org.cen.simulRobot.brain.gripper.GripperSimulHandler;
import org.cen.simulRobot.brain.navigation.NavigationSimulHandler;
import org.cen.simulRobot.brain.sonar.SonarSimulHandler;
import org.cen.simulRobot.brain.timer.TimerSimulHandler;
import org.cen.simulRobot.brain.vision.VisionSimulHandler;
import org.cen.simulRobot.device.timer.MatchStartedSimulRequest;
import org.cen.simulRobot.match.ASimulMatchStrategy;
import org.cen.simulRobot.match.simulOpponent.OpponentElemenHandler;
import org.cen.ui.gameboard.IGameBoardService;
import org.cen.ui.gameboard.ISimulGameBoard;
import org.cen.ui.gameboard.elements.SimulOpponentElement;
import org.cen.ui.gameboard.elements.SimulRobotElement;
import org.cen.util.ReflectionUtils;

public class SimulMatchStrategy2011 extends ASimulMatchStrategy {
	private static final boolean BEACON_ENABLE = false;
	private static final boolean SONAR_ENABLE = true;
	private static final boolean VISION_ENABLE = true;
	private BeaconSimulHandler beaconHandler;
	private GripperSimulHandler gripperHandler;
	private boolean matchStarted;
	private OpponentElemenHandler opponentElementHandler;
	private SonarSimulHandler sonarHandler;
	protected TimerSimulHandler timerHandler;
	private VisionSimulHandler visionHandler;



	private void fulRestart(){
		//full restart
		Collection<IRobotService> services = servicesProvider.getServices();
		for (IRobotService service : services) {
			ReflectionUtils.invoke(PreDestroy.class, service, null);
		}

		IRobotFactory factory = servicesProvider.getService(IRobotFactory.class);
		factory.restart();

		for (IRobotService service : services) {
			ReflectionUtils.invoke(PostConstruct.class, service, null);
		}

		for (IRobotService service : services) {
			if (service instanceof IRobotServiceInitializable) {
				((IRobotServiceInitializable) service).afterRegister();
			}
		}
		matchStarted = false;
	}

	@Override
	protected void specificAfterRegister() {
		this.timerHandler = new TimerSimulHandler(servicesProvider);
	}

	@Override
	protected void specificNotifyEvent(IMatchEvent event) {
		if(event instanceof RobotInitializedEvent){
			shutdown();
			fulRestart();
			this.opponentElementHandler = new OpponentElemenHandler(servicesProvider);
			if(VISION_ENABLE){
				this.visionHandler = new VisionSimulHandler(servicesProvider);
			}

			this.gripperHandler = new GripperSimulHandler(servicesProvider);
			this.navigationHandler = new NavigationSimulHandler(servicesProvider);

			IComService comService = servicesProvider.getService(IComService.class);
			comService.reconnect();

		}else if (event instanceof MatchStartedEvent){
			if(!matchStarted){
				this.dispatcher.sendRequest(new MatchStartedSimulRequest());

				if(SONAR_ENABLE){
					this.sonarHandler = new SonarSimulHandler(servicesProvider);
					this.sonarHandler.start();
				}
				if(BEACON_ENABLE){
					this.beaconHandler = new BeaconSimulHandler(servicesProvider);
					this.beaconHandler.start();
				}
				opponentElementHandler.handleEvent(event);
				matchStarted = true;
			}
		}
		else if (event instanceof MatchFinishedEvent){
			shutdown();
			afterRegister();
		}
	}

	@Override
	//@PreDestroy
	protected void specificShutdown() {

		ISimulGameBoard gameBoard = (ISimulGameBoard)servicesProvider.getService(IGameBoardService.class);
		gameBoard.getElements().removeAll(gameBoard.getElements(SimulOpponentElement.class));
		gameBoard.getElements().removeAll(gameBoard.getElements(SimulRobotElement.class));
		gameBoard.getElements().removeAll(gameBoard.getElements(SimulPawnElement.class));

		if (gripperHandler != null) {
			gripperHandler.shutdown();
			gripperHandler = null;
		}
		if (timerHandler != null) {
			timerHandler.shutdown();
			timerHandler = null;
		}

		if (opponentElementHandler != null) {
			opponentElementHandler.shutdown();
			opponentElementHandler = null;
		}
		if (visionHandler != null) {
			visionHandler.shutdown();
			visionHandler = null;
		}
		if (sonarHandler != null) {
			sonarHandler.shutdown();
			sonarHandler = null;
		}

		if (beaconHandler != null) {
			beaconHandler.shutdown();
			beaconHandler = null;
		}
	}
}
