package org.cen.ui.web;

import java.util.logging.Logger;

import org.cen.cup.cup2008.device.launcher.LauncherHandler;
import org.cen.cup.cup2008.device.launcher.LauncherLaunchRequest;
import org.cen.cup.cup2008.device.launcher.LauncherLoaderRequest;
import org.cen.cup.cup2008.device.launcher.LauncherLoaderRequest.LoaderAction;
import org.cen.cup.cup2008.device.launcher.LauncherMoveRequest;
import org.cen.cup.cup2008.device.launcher.LauncherMoveRequest.LauncherMove;
import org.cen.logging.LoggingUtils;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.request.IDeviceRequestDispatcher;
import org.cen.robot.device.request.IRobotDeviceRequest;
import org.cen.robot.services.IRobotService;
import org.cen.robot.services.IRobotServiceProvider;

public class LaunchView implements IRobotService {
    private static final Logger LOGGER = LoggingUtils.getClassLogger();

    private IRobotServiceProvider servicesProvider;

    public void lock() {
        sendRequest(new LauncherLoaderRequest(LoaderAction.LOCK));
    }

    public void close() {
        sendRequest(new LauncherMoveRequest(LauncherMove.CLOSE));
    }

    public void down() {
        sendRequest(new LauncherMoveRequest(LauncherMove.DOWN));
    }

    private IDeviceRequestDispatcher getDispatcher() {
        IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
        return handler.getRequestDispatcher();
    }

    public void launch() {
        LOGGER.finest("launching");
        sendRequest(new LauncherLaunchRequest());
    }

    public void open() {
        sendRequest(new LauncherMoveRequest(LauncherMove.OPEN));
    }

    private void sendRequest(IRobotDeviceRequest request) {
        getDispatcher().sendRequest(request);
    }

    @Override
    public void setServicesProvider(IRobotServiceProvider provider) {
        servicesProvider = provider;
    }

    public void unlock() {
        sendRequest(new LauncherLoaderRequest(LoaderAction.UNLOCK));
    }

    public void up() {
        sendRequest(new LauncherMoveRequest(LauncherMove.UP));
    }

    public void sequence1() {
        LauncherHandler handler = new LauncherHandler(servicesProvider);
        handler.sequence1();
    }
}
