package org.cen.cup.cup2011.device.gripper2011;

import org.cen.robot.device.request.impl.RobotDeviceRequest;

public abstract class GripperRequest2011 extends RobotDeviceRequest {

    public GripperRequest2011() {
        super(Gripper2011Device.NAME);
    }
}
