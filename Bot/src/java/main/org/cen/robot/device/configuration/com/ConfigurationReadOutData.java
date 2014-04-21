package org.cen.robot.device.configuration.com;

import org.cen.com.documentation.DeviceDataSignature;
import org.cen.com.documentation.DeviceMethodSignature;
import org.cen.com.documentation.DeviceMethodType;
import org.cen.com.out.OutData;
import org.cen.robot.device.configuration.ConfigurationDevice;

/**
 * Serial data for reading the configuration device.
 * 
 * @author Emmanuel ZURMELY
 */
//@formatter:off
@DeviceDataSignature(
        deviceName = ConfigurationDevice.NAME,
        methods = { 
                @DeviceMethodSignature(
                        header = ConfigurationReadOutData.HEADER,
                        methodName = "configuration",
                        type = DeviceMethodType.INPUT,
                        parameters = {}
                )
})
//@formatter:on
public class ConfigurationReadOutData extends OutData {

    static final String HEADER = "c";

    @Override
    public String getHeader() {
        return HEADER;
    }
}
