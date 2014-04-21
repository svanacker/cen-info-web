package org.cen.cup.cup2010.ui.web;

import java.util.EnumSet;

import org.cen.cup.cup2010.device.specific2010.CollectCorn2010Request;
import org.cen.cup.cup2010.device.specific2010.CollectCorn2010Request.Action;
import org.cen.cup.cup2010.device.specific2010.CollectCorn2010Request.Side;
import org.cen.cup.cup2010.device.specific2010.CollectOrange2010Request;
import org.cen.cup.cup2010.device.specific2010.CollectTomato2010Request;
import org.cen.cup.cup2010.device.specific2010.ReleaseObjects2010Request;
import org.cen.cup.cup2010.device.specific2010.RobotLift2010Request;
import org.cen.robot.IRobotService;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.device.DeviceRequestDispatcher;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.RobotDeviceRequest;

/**
 * Backing bean for tha actions view.
 * 
 * @author Emmanuel ZURMELY
 */
public class Actions2010View implements IRobotService {

    private IRobotServiceProvider servicesProvider;

    private void collectCorn(Side side, Action action) {
        sendRequest(new CollectCorn2010Request(side, action));
    }

    public void cornLeftDown() {
        collectCorn(Side.LEFT, Action.DOWN);
    }

    public void cornLeftCollect() {
        collectCorn(Side.LEFT, Action.COLLECT);
    }

    public void cornRightDown() {
        collectCorn(Side.RIGHT, Action.DOWN);
    }

    public void cornRightCollect() {
        collectCorn(Side.RIGHT, Action.COLLECT);
    }

    private DeviceRequestDispatcher getDispatcher() {
        IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
        return handler.getRequestDispatcher();
    }

    public void pickOrange() {
        sendRequest(new CollectOrange2010Request(CollectOrange2010Request.Action.PICK));
    }

    public void releaseObjects() {
        sendRequest(new ReleaseObjects2010Request(EnumSet.allOf(ReleaseObjects2010Request.Target.class)));
    }

    public void robotDown() {
        sendRequest(new RobotLift2010Request(RobotLift2010Request.Action.DOWN));
    }

    public void robotUp() {
        sendRequest(new RobotLift2010Request(RobotLift2010Request.Action.UP));
    }

    private void sendRequest(RobotDeviceRequest request) {
        getDispatcher().sendRequest(request);
    }

    @Override
    public void setServicesProvider(IRobotServiceProvider provider) {
        servicesProvider = provider;
    }

    public void tomatoOn() {
        sendRequest(new CollectTomato2010Request(CollectTomato2010Request.Action.ON));
    }

    public void tomatoOff() {
        sendRequest(new CollectTomato2010Request(CollectTomato2010Request.Action.OFF));
    }
}
