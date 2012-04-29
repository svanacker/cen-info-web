package org.cen.simulRobot.brain.configuration;

import org.cen.robot.IRobot;
import org.cen.robot.IRobotFactory;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.brain.AbstractDeviceHandler;
import org.cen.robot.device.DeviceRequestDispatcher;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.RobotDeviceResult;
import org.cen.simulRobot.RobotSwitches;
import org.cen.simulRobot.device.configuration.ConfigurationSimulDevice;
import org.cen.simulRobot.device.configuration.ConfigurationSimulReadRequest;
import org.cen.simulRobot.device.configuration.ConfigurationSimulReadResult;

/**
 * Send the configuration of the robot to the motherBoard when it's require
 * 
 * @author Benouamer Omar
 */
public class ConfigurationSimulHandler extends AbstractDeviceHandler {

    /**
     * Constructor.
     * 
     * @param servicesProvider the services provider
     */
    public ConfigurationSimulHandler(IRobotServiceProvider servicesProvider) {
        super(servicesProvider);
    }

    @Override
    public String getDeviceName() {
        return ConfigurationSimulDevice.NAME;
    }

    @Override
    public void handleResult(RobotDeviceResult result) {
        if (result instanceof ConfigurationSimulReadResult) {
            updateConfiguration();
        }
    }

    /**
     * Updates the configuration and notifies the match strategy handler of the change.
     * 
     * @param result the data resulting from the configuration read
     */
    protected void updateConfiguration() {

        // recupere les interrupteurs
        IRobotFactory robotFactory = servicesProvider.getService(IRobotFactory.class);
        IRobot robot = robotFactory.getRobot();
        RobotSwitches robotSwitches = robot.getAttribute(RobotSwitches.class);
        int switches = robotSwitches.getSwitches();
        DeviceRequestDispatcher dispatcher =
                servicesProvider.getService(IRobotDevicesHandler.class).getRequestDispatcher();
        dispatcher.sendRequest(new ConfigurationSimulReadRequest(switches));

    }
}
