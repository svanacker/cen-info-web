package org.cen.simulRobot.match;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.cen.logging.LoggingUtils;
import org.cen.robot.IRobotServiceInitializable;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.device.DeviceRequestDispatcher;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.match.IMatchEvent;
import org.cen.simulRobot.brain.configuration.ConfigurationSimulHandler;
import org.cen.simulRobot.brain.navigation.NavigationSimulHandler;
import org.cen.simulRobot.match.event.AMovingHandlerEvent;
import org.cen.ui.gameboard.IGameBoardService;
import org.cen.ui.gameboard.ISimulGameBoard;

public abstract class ASimulMatchStrategy implements ISimulMatchStrategy, IRobotServiceInitializable {

	private static final Logger LOGGER = LoggingUtils.getClassLogger();

	private ConfigurationSimulHandler configurationHandler;
	protected DeviceRequestDispatcher dispatcher;
	protected NavigationSimulHandler navigationHandler;

	protected IRobotServiceProvider servicesProvider;

	public ASimulMatchStrategy(){
		LOGGER.config("initializing StrategieDevice " + getClass().getSimpleName());
	}

	@Override
	public void afterRegister() {
		this.dispatcher = servicesProvider.getService(IRobotDevicesHandler.class).getRequestDispatcher();
		// initialisation of handlers Devices
		this.configurationHandler = new ConfigurationSimulHandler(servicesProvider);
		specificAfterRegister();
	}


	@Override
	public NavigationSimulHandler getNavigationHandler() {
		return navigationHandler;
	}

	@PostConstruct
	protected void initialize(){
		System.out.println("appel du postConstruct dans simulstrategie");
	}

	@Override
	public void notifyEvent(IMatchEvent event){
		if (event instanceof AMovingHandlerEvent){
			ISimulGameBoard gameboard = (ISimulGameBoard)servicesProvider.getService(IGameBoardService.class);
			gameboard.notifyEvent((AMovingHandlerEvent)event);
		}else{
			specificNotifyEvent(event);
		}
	}

	@Override
	public void setServicesProvider(IRobotServiceProvider provider) {
		this.servicesProvider = provider;
		provider.registerService(ISimulMatchStrategy.class, this);
	}

	@Override
	public void shutdown() {
		if (configurationHandler != null) {
			configurationHandler.shutdown();
			configurationHandler = null;
		}
		if (navigationHandler != null) {
			navigationHandler.shutdown();
			navigationHandler = null;
		}
		specificShutdown();
	}

	protected abstract void specificAfterRegister();

	protected abstract void specificNotifyEvent(IMatchEvent event);

	protected abstract void specificShutdown();
}
