package org.cen.robot.match.strategy.impl;

import org.cen.robot.device.request.IRobotDeviceRequest;
import org.cen.robot.match.strategy.ITargetActionItem;

public class SimpleTargetActionItem implements ITargetActionItem {

    private final IRobotDeviceRequest request;

    public SimpleTargetActionItem(IRobotDeviceRequest request) {
        super();
        this.request = request;
    }

    @Override
    public IRobotDeviceRequest getRequest() {
        return request;
    }
}
