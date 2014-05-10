package org.cen.robot.brain;

import org.cen.robot.device.IRobotDeviceListener;
import org.cen.robot.device.RobotDeviceResult;
import org.cen.robot.device.navigation.INavigationDevice;
import org.cen.robot.device.navigation.NavigationResult;
import org.cen.robot.match.IMatchStrategy;
import org.cen.robot.services.IRobotServiceProvider;

public class NavigationHandler implements IRobotDeviceListener {

    private final IRobotServiceProvider servicesProvider;

    public NavigationHandler(IRobotServiceProvider servicesProvider) {
        super();
        this.servicesProvider = servicesProvider;
    }

    @Override
    public String getDeviceName() {
        return INavigationDevice.NAME;
    }

    private void handleNavigationResult(NavigationResult result) {
        IMatchStrategy strategy = servicesProvider.getService(IMatchStrategy.class);
        if (strategy == null) {
            return;
        }
        switch (result.getStatus()) {
        case EMPTYQUEUE:
            // strategy.done();
            break;
        case INTERRUPTED:
            // strategy.done();
            break;
        }
    }

    @Override
    public void handleResult(RobotDeviceResult result) {
        if (result instanceof NavigationResult) {
            handleNavigationResult((NavigationResult) result);
        }
    }
}
