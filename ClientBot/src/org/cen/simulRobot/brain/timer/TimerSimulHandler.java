package org.cen.simulRobot.brain.timer;

import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.brain.AbstractDeviceHandler;
import org.cen.robot.device.RobotDeviceResult;
import org.cen.robot.match.events.MatchFinishedEvent;
import org.cen.robot.match.events.MatchStartedEvent;
import org.cen.robot.match.events.RobotInitializedEvent;
import org.cen.simulRobot.device.timer.MatchFinishedSimulResult;
import org.cen.simulRobot.device.timer.MatchStartSimulResult;
import org.cen.simulRobot.device.timer.RobotInitializingSimulResult;
import org.cen.simulRobot.device.timer.TimerSimulDevice;
import org.cen.simulRobot.match.ISimulMatchStrategy;

public class TimerSimulHandler extends AbstractDeviceHandler {
    /**
     * Constructor.
     * 
     * @param servicesProvider the services provider
     */
    public TimerSimulHandler(IRobotServiceProvider servicesProvider) {
        super(servicesProvider);
    }

    @Override
    public String getDeviceName() {
        return TimerSimulDevice.NAME;
    }

    @Override
    public void handleResult(RobotDeviceResult result) {
        if (result instanceof RobotInitializingSimulResult) {
            ISimulMatchStrategy simulEnvironement = servicesProvider.getService(ISimulMatchStrategy.class);
            simulEnvironement.notifyEvent(new RobotInitializedEvent());
        } else if (result instanceof MatchStartSimulResult) {
            ISimulMatchStrategy simulEnvironement = servicesProvider.getService(ISimulMatchStrategy.class);
            simulEnvironement.notifyEvent(new MatchStartedEvent());
        } else if (result instanceof MatchFinishedSimulResult) {
            ISimulMatchStrategy simulMatchStrategy = servicesProvider.getService(ISimulMatchStrategy.class);
            simulMatchStrategy.notifyEvent(new MatchFinishedEvent());
        }
    }
}
