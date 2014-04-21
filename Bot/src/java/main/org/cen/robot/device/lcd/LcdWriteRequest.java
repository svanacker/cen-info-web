package org.cen.robot.device.lcd;

import org.cen.robot.device.RobotDeviceRequest;

public class LcdWriteRequest extends RobotDeviceRequest {

    private String text;

    public LcdWriteRequest(String text) {
        super(LcdDevice.NAME);
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
