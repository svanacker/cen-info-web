package org.cen.robot.brain;

import java.util.Collection;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.cen.logging.LoggingUtils;
import org.cen.robot.device.RobotDeviceResult;
import org.cen.robot.device.timer.MatchFinishedResult;
import org.cen.robot.device.timer.MatchStartedResult;
import org.cen.robot.device.timer.RobotInitializedResult;
import org.cen.robot.device.timer.SleepResult;
import org.cen.robot.device.timer.TimerDevice;
import org.cen.robot.factory.IRobotFactory;
import org.cen.robot.match.IMatchEvent;
import org.cen.robot.match.IMatchStrategy;
import org.cen.robot.match.events.MatchFinishedEvent;
import org.cen.robot.match.events.MatchStartedEvent;
import org.cen.robot.match.events.RobotInitializedEvent;
import org.cen.robot.match.events.TimerEvent;
import org.cen.robot.services.IRobotService;
import org.cen.robot.services.IRobotServiceInitializable;
import org.cen.robot.services.IRobotServiceProvider;
import org.cen.utils.ReflectionUtils;

/**
 * Handler of the Timer device.
 * 
 * @author Emmanuel ZURMELY
 */
public class TimeHandler extends AbstractDeviceHandler {

	private static final Logger LOGGER = LoggingUtils.getClassLogger();

	protected static final long TIMER_MATCH_DURATION = 89000;

	private static boolean started = false;

	private Thread timerThread;

	private boolean timerThreadTerminated = false;

	private long startTime;

	/**
	 * Constructor.
	 * 
	 * @param servicesProvider
	 */
	public TimeHandler(IRobotServiceProvider servicesProvider) {
		super(servicesProvider);
	}

	private void fullRestart() {
		LOGGER.info("full restart");
		Collection<IRobotService> services = servicesProvider.getServices();
		for (IRobotService service : services) {
			LOGGER.finest("shutting down " + service.getClass().getSimpleName());
			ReflectionUtils.invoke(PreDestroy.class, service, null);
		}

		LOGGER.finest("restarting robot");
		IRobotFactory factory = servicesProvider.getService(IRobotFactory.class);
		factory.restart();

		for (IRobotService service : services) {
			LOGGER.finest("initializing " + service.getClass().getSimpleName());
			ReflectionUtils.invoke(PostConstruct.class, service, null);
		}

		for (IRobotService service : services) {
			if (service instanceof IRobotServiceInitializable) {
				LOGGER.finest("post registration of " + service.getClass().getSimpleName());
				((IRobotServiceInitializable) service).afterRegister();
			}
		}
	}

	@Override
	public String getDeviceName() {
		return TimerDevice.NAME;
	}

	public double getElapsedTime() {
		return 0.001 * (System.currentTimeMillis() - startTime);
	}

	public long getStartTime() {
		return startTime;
	}

	@Override
	public void handleResult(RobotDeviceResult result) {
		IMatchStrategy strategy = servicesProvider.getService(IMatchStrategy.class);
		if (result instanceof SleepResult) {
			strategy.notifyEvent(new TimerEvent((SleepResult) result));
		} else if (result instanceof MatchStartedResult) {
			startTimerThread();
			strategy.notifyEvent(new MatchStartedEvent());
		} else if (result instanceof MatchFinishedResult) {
			strategy.notifyEvent(new MatchFinishedEvent());
		} else if (result instanceof RobotInitializedResult) {
			if (started) {
				fullRestart();
			}
			strategy.notifyEvent(new RobotInitializedEvent());
			started = true;
		}
	}

	private void notifyStrategy(IMatchEvent event) {
		IMatchStrategy strategy = servicesProvider.getService(IMatchStrategy.class);
		strategy.notifyEvent(event);
	}

	@Override
	public void shutdown() {
		stopTimerThread();
		super.shutdown();
	}

	private void startTimerThread() {
		stopTimerThread();
		timerThread = new Thread(new Runnable() {
			@Override
			public void run() {
				startTime = System.currentTimeMillis();
				while (!timerThreadTerminated) {
					try {
						Thread.sleep(1000);
						long currentTime = System.currentTimeMillis();
						if (currentTime - startTime >= TIMER_MATCH_DURATION) {
							timerThreadTerminated = true;
							notifyStrategy(new MatchFinishedEvent());
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}, "timerThread");
		timerThreadTerminated = false;
		timerThread.start();
	}

	private void stopTimerThread() {
		if (timerThread != null) {
			timerThreadTerminated = true;
			timerThread.interrupt();
			timerThread = null;
			Thread.yield();
		}
	}
}
