package org.cen.robot.device.vision;

import org.cen.robot.device.request.impl.RobotDeviceRequest;

public abstract class VisionRequest extends RobotDeviceRequest {

    public VisionRequest() {
        super(AbstractVisionDevice.NAME);
    }
}
