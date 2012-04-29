package org.cen.simulRobot.brain.lcd;

import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.brain.AbstractDeviceHandler;
import org.cen.robot.device.RobotDeviceResult;
import org.cen.simulRobot.device.lcd.LCDSimulDevice;
import org.cen.simulRobot.device.lcd.LCDSimulReadResult;

/**
 * Abstract base class of the lcd handler. The configuration handler handles the lcd event notified by the lcd device
 * 
 * @author Omar BENOUAMER
 */
public class LCDSimulHandler extends AbstractDeviceHandler {
    /**
     * Constructor.
     * 
     * @param servicesProvider the services provider
     */
    public LCDSimulHandler(IRobotServiceProvider servicesProvider) {
        super(servicesProvider);
    }

    @Override
    public String getDeviceName() {
        return LCDSimulDevice.NAME;
    }

    @Override
    public void handleResult(RobotDeviceResult result) {
        if (result instanceof LCDSimulReadResult) {

        }
    }

}
