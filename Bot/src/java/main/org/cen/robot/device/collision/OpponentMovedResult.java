package org.cen.robot.device.collision;

import org.cen.robot.device.collision.com.OpponentPositionInData;
import org.cen.robot.device.request.IRobotDeviceRequest;

public class OpponentMovedResult extends CollisionResult {

    private final OpponentPositionInData position;

    public OpponentMovedResult(IRobotDeviceRequest request, OpponentPositionInData position) {
        super(request);
        this.position = position;
    }

    public OpponentPositionInData getData() {
        return position;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[position=" + position + "]";
    }
}
