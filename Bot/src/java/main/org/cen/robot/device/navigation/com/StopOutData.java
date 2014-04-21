package org.cen.robot.device.navigation.com;

import org.cen.com.documentation.DeviceDataSignature;
import org.cen.com.documentation.DeviceMethodSignature;
import org.cen.com.documentation.DeviceMethodType;
import org.cen.com.out.OutData;
import org.cen.robot.device.navigation.NavigationDevice;

/**
 * Data sent to the device for stopping the robot in movement.
 * 
 * @author Emmanuel ZURMELY
 */
@DeviceDataSignature(deviceName = NavigationDevice.NAME, methods = {
    @DeviceMethodSignature(header = StopOutData.HEADER, parameters = {}, type = DeviceMethodType.INPUT)
})
public class StopOutData extends OutData {

    protected static final String HEADER = "Z";

    @Override
    public String getHeader() {
        return HEADER;
    }
}
