package org.cen.cup.cup2011.device.vision2011;

import org.cen.robot.device.request.impl.RobotDeviceRequest;

public class LookForPawnRequest extends RobotDeviceRequest {

    private final boolean initial;

    /**
     * Constructor.
     * 
     * @param initial
     *            true if initial look up (before match start)
     */
    public LookForPawnRequest(boolean initial) {
        super(Vision2011Device.NAME);
        this.initial = initial;
    }

    public boolean getInitial() {
        return initial;
    }
}
