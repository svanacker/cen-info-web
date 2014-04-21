package org.cen.robot.device.pid.com;

import org.cen.com.ComDataUtils;
import org.cen.com.documentation.DeviceDataSignature;
import org.cen.com.documentation.DeviceMethodSignature;
import org.cen.com.documentation.DeviceMethodType;
import org.cen.com.documentation.DeviceParameter;
import org.cen.com.documentation.DeviceParameterType;
import org.cen.com.out.OutData;
import org.cen.robot.device.navigation.NavigationDevice;

/**
 * Encapsulation of the message which ask for PID.
 */
//@formatter:off
@DeviceDataSignature(deviceName = NavigationDevice.NAME, methods = { 
        @DeviceMethodSignature(
                header = ReadPIDOutData.HEADER,
                methodName = "readPID",
                type = DeviceMethodType.INPUT,
                parameters = {
                        @DeviceParameter(name = "index", length = 2, type = DeviceParameterType.SIGNED, unit = "")
                })
        })
//@formatter:on
public class ReadPIDOutData extends OutData {

    final static String HEADER = "q";

    protected int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Constructor.
     */
    public ReadPIDOutData(int index) {
        super();
        this.index = index;
    }

    @Override
    public String getArguments() {
        String result = ComDataUtils.format(index, 2);
        return result;
    }

    @Override
    public String getHeader() {
        return HEADER;
    }
}
