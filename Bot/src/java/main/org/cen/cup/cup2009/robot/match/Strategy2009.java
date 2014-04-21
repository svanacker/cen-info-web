package org.cen.cup.cup2009.robot.match;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.cen.com.IComService;
import org.cen.com.out.OutData;
import org.cen.cup.cup2009.device.Specific2009Device;
import org.cen.cup.cup2009.device.Specific2009Handler;
import org.cen.cup.cup2009.device.Specific2009Request;
import org.cen.cup.cup2009.navigation.NavigationHandler2009;
import org.cen.logging.LoggingUtils;
import org.cen.robot.IRobotConfiguration;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.RobotPosition;
import org.cen.robot.RobotUtils;
import org.cen.robot.brain.TimeHandler;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.RobotDeviceRequest;
import org.cen.robot.device.configuration.ConfigurationReadRequest;
import org.cen.robot.device.lcd.com.LcdPrintOutData;
import org.cen.robot.device.navigation.NavigationDevice;
import org.cen.robot.device.navigation.NavigationRequest;
import org.cen.robot.device.navigation.StopRequest;
import org.cen.robot.device.navigation.com.MoveOutData;
import org.cen.robot.device.navigation.com.StopOutData;
import org.cen.robot.match.IMatchEvent;
import org.cen.robot.match.IMatchStrategy;
import org.cen.robot.match.IMatchStrategyHandler;
import org.cen.robot.match.MatchData;
import org.cen.robot.match.events.MatchConfigurationDone;
import org.cen.robot.match.events.MatchFinishedEvent;
import org.cen.robot.match.events.MatchStartedEvent;
import org.cen.robot.match.events.RobotInitializedEvent;
import org.cen.util.Holder;
import org.cen.util.StringConstants;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class Strategy2009 implements IMatchStrategyHandler, ResourceLoaderAware {
	private static final Logger LOGGER = LoggingUtils.getClassLogger();

	public static final String PROPERTY_INITIAL_POSITION = "initialPosition";

	static final int TRAJECTORY_HOMOLOGATION = 0;

	private ConfigurationAnalyzer configurationAnalyzer;

	private Strategy2009Context fsm;

	private Timer matchTimer;

	private NavigationHandler2009 navigationHandler;

	private ResourceLoader resourcesLoader;

	private String resourcesPath;

	private IRobotServiceProvider servicesProvider;

	private void buildOutData(List<OutData> list, RobotDeviceRequest request) {
		if (request instanceof NavigationRequest) {
			List<OutData> outData = getOutData(new StopRequest());
			list.addAll(outData);
			outData = getOutData((NavigationRequest) request);
			list.addAll(outData);
		} else if (request instanceof Specific2009Request) {
			List<OutData> outData = getOutData((Specific2009Request) request);
		}
	}

	public void doConfiguration() {
		new ConfigurationHandler2009(servicesProvider);
		configurationAnalyzer = new ConfigurationAnalyzer(servicesProvider);
		new Specific2009Handler(servicesProvider);
		LOGGER.fine("Waiting for match configuration");
		IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
		handler.getRequestDispatcher().sendRequest(new ConfigurationReadRequest());
	}

	public void doStartTrajectory() {
		LOGGER.fine("Sending trajectory");

		navigationHandler.startTrajectory();
	}

	public void doStopTrajectory() {
		// Stops the navigation handler
		navigationHandler.shutdown();
		navigationHandler = null;
		// Stop motors
		IComService com = servicesProvider.getService(IComService.class);
		com.writeOutData(new StopOutData());

		LOGGER.fine("Match stopped");
	}

	public void doWaitForGameboardAnalysis() {
		LOGGER.fine("Waiting for gameboard analysis");

		Properties properties = new Properties();
		Resource resource = resourcesLoader.getResource(resourcesPath);
		try {
			InputStream is = resource.getInputStream();
			try {
				properties.load(is);
			} finally {
				is.close();
			}
		} catch (Exception e) {
			LOGGER.warning("unable to load properties: " + e.getMessage());
		}

		navigationHandler = new NavigationHandler2009(servicesProvider);
		navigationHandler.setProperties(properties);

		configurationAnalyzer.start();
		fsm.GameboardAnalyzed();
	}

	public void doWaitForMatchStart() {
		LOGGER.fine("Waiting for match start");
	}

	public void doWaitForRobotInitialization() {
		LOGGER.fine("Waiting for robot initialization");
		new TimeHandler(servicesProvider);
	}

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	private List<OutData> getOutData(NavigationRequest request) {
		IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
		NavigationDevice device = (NavigationDevice) handler.getDevice(NavigationDevice.NAME);
		return device.getOutData(request);
	}

	private List<OutData> getOutData(Specific2009Request request) {
		IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
		Specific2009Device device = (Specific2009Device) handler.getDevice(Specific2009Device.NAME);
		return device.getOutData(request);
	}

	@Override
	public boolean handleEvent(IMatchEvent event) {
		if (event instanceof RobotInitializedEvent) {
			LOGGER.fine("Robot initialized");
			fsm.RobotInitializationDone();
		} else if (event instanceof MatchConfigurationDone) {
			LOGGER.fine("Configuration done");
			fsm.ConfigurationDone();
		} else if (event instanceof GameboardConfigurationReadEvent) {
			setConfiguration(((GameboardConfigurationReadEvent) event).getCard());
			// fsm.GameboardAnalyzed();
		} else if (event instanceof MatchStartedEvent) {
			startMatch();
		} else if (event instanceof MatchFinishedEvent) {
			stopMatch();
		} else {
			return false;
		}
		return true;
	}

	@PostConstruct
	public void initialize() {
		registerHandler();
	}

	private void registerHandler() {
		if (servicesProvider == null) {
			return;
		}

		IMatchStrategy strategy = servicesProvider.getService(IMatchStrategy.class);
		strategy.addHandler(this);
	}

	private void setConfiguration(int card) {
		LOGGER.fine("Gameboard configuration: " + card);
		// Displaying configuration on LCD
		writeLCD(StringConstants.STR_CR + "Configuration: " + String.valueOf(card));

		int phases = navigationHandler.getPhasesCount();

		// Initializing the navigation handler
		List<RobotDeviceRequest> requests = new ArrayList<RobotDeviceRequest>();
		MatchData matchData = RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);
		StringBuilder b = new StringBuilder();
		Holder<Double> orientation = new Holder<Double>(Double.NaN);
		for (int phase = 0; phase < phases; phase++) {
			b.setLength(0);
			List<RobotDeviceRequest> phaseRequests = navigationHandler.getRequests(card, phase, matchData.getProperty(MatchData2009.PROPERTY_TRAJECTORY_SUFFIX), orientation);
			requests.addAll(phaseRequests);
			List<OutData> s = new ArrayList<OutData>();
			for (RobotDeviceRequest request : phaseRequests) {
				// build out data
				buildOutData(s, request);
				// build out data messages list
				for (OutData outData : s) {
					if (outData instanceof MoveOutData) {
						b.append(outData.getMessage());
						b.append("<br />");
					}
				}
				s.clear();
			}
			matchData.put("trajectoryData" + phase, b.toString());
		}
		matchData.put("requests", requests);
		matchData.put("analyzedCard", card);
	}

	void setInitialPosition() {
		RobotPosition position = RobotUtils.getRobotAttribute(RobotPosition.class, servicesProvider);
		MatchData data = RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);
		IRobotConfiguration configuration = RobotUtils.getRobot(servicesProvider).getConfiguration();
		Properties properties = configuration.getProperties();
		position.setFromProperties(properties, PROPERTY_INITIAL_POSITION + "." + data.getSide() + ".");

		writeLCD(StringConstants.STR_CLS + "Side: " + data.getSide());
	}

	@Override
	public void setResourceLoader(ResourceLoader resourcesLoader) {
		this.resourcesLoader = resourcesLoader;
	}

	public void setResourcesPath(String resourcesPath) {
		this.resourcesPath = resourcesPath;
	}

	public void setServicesProvider(IRobotServiceProvider provider) {
		servicesProvider = provider;
		registerHandler();
	}

	@PreDestroy
	public void shutdown() {
		IMatchStrategy strategy = servicesProvider.getService(IMatchStrategy.class);
		strategy.removeHandler(this);
		if (matchTimer != null) {
			matchTimer.cancel();
			matchTimer = null;
		}
		if (navigationHandler != null) {
			navigationHandler.shutdown();
			navigationHandler = null;
		}
	}

	@Override
	public void start() {
		LOGGER.config("Initializing match strategy");
		fsm = new Strategy2009Context(this);
		LOGGER.config("Match strategy started");
		fsm.Start();
	}

	private void startMatch() {
		LOGGER.fine("Match started");
		writeLCD(StringConstants.STR_CLS + "Start");
		MatchData2009 data = (MatchData2009) RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);
		writeLCD(StringConstants.STR_CR + "Trajectory: " + data.getGameBoardConfiguration());
		configurationAnalyzer.done();
		startMatchCounter();
		fsm.MatchStarted();
	}

	private void startMatchCounter() {
		MatchData data = RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);
		long delay = data.getDuration();

		LOGGER.fine("match ending in " + delay + " ms");

		TimerTask matchTimerTask = new TimerTask() {
			@Override
			public void run() {
				handleEvent(new MatchFinishedEvent());
			}
		};

		matchTimer = new Timer("MatchTimer");
		matchTimer.schedule(matchTimerTask, delay);
	}

	@Override
	public void stop() {
		LOGGER.config("Stopping match strategy");
		shutdown();
		LOGGER.config("Match strategy stopped");
	}

	private void stopMatch() {
		LOGGER.fine("Stopping match");
		writeLCD(StringConstants.STR_CLS + "Stop");
		fsm.MatchStopped();
	}

	public void unhandled() {
		LOGGER.warning("Unhandled transition: " + fsm.getTransition());
	}

	private void writeLCD(String s) {
		IComService comService = servicesProvider.getService(IComService.class);
		comService.writeOutData(new LcdPrintOutData(s));
	}
}
