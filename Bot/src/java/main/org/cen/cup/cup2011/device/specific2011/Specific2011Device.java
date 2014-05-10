package org.cen.cup.cup2011.device.specific2011;

import org.cen.com.IComService;
import org.cen.com.in.InData;
import org.cen.com.in.InDataListener;
import org.cen.robot.device.AbstractRobotDevice;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.navigation.com.StopOutData;
import org.cen.robot.device.request.IRobotDeviceRequest;

public class Specific2011Device extends AbstractRobotDevice implements InDataListener {
    public static final String NAME = "specific2011";

    public Specific2011Device() {
        super(NAME);
    }

    @Override
    public void debug(String debugAction) {
        IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
        if (debugAction.equals("nextStep")) {
            handler.getRequestDispatcher().nextStep();
        } else if (debugAction.equals("stepByStepOn")) {
            handler.getRequestDispatcher().setStepByStep(true);
        } else if (debugAction.equals("stepByStepOff")) {
            handler.getRequestDispatcher().setStepByStep(false);
        } else if (debugAction.equals("resetPosition")) {
            IComService comService = servicesProvider.getService(IComService.class);
            comService.writeOutData(new StopOutData());
        }
    }

    @Override
    public void onInData(InData data) {
    }

    @Override
    protected void internalHandleRequest(IRobotDeviceRequest request) {
    }
}
