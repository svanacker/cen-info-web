package org.cen.robot.device.battery;

import java.util.HashSet;
import java.util.Set;

import org.cen.com.DefaultDecoder;
import org.cen.com.IllegalComDataException;
import org.cen.com.documentation.DeviceDataSignature;
import org.cen.com.documentation.DeviceMethodSignature;
import org.cen.com.documentation.DeviceMethodType;
import org.cen.com.documentation.DeviceParameter;
import org.cen.com.documentation.DeviceParameterType;
import org.cen.com.in.InData;
import org.cen.robot.device.battery.com.BatteryReadInData;

/**
 * Decode the message of battery value.
 */
@DeviceDataSignature(deviceName = "Battery", methods = { @DeviceMethodSignature(header = BatteryReadInData.HEADER, type = DeviceMethodType.OUTPUT, parameters = { @DeviceParameter(name = "value", length = 4, type = DeviceParameterType.UNSIGNED, unit = "milliVolt") }) })
public class BatteryDataDecoder extends DefaultDecoder {

    final static Set<String> handled = new HashSet<String>();

    static {
        handled.add(BatteryReadInData.HEADER);
    }

    @Override
    public InData decode(String data) throws IllegalComDataException {
        checkLength(BatteryReadInData.HEADER, data);
        int value = Integer.parseInt(data.substring(1, 5), 16);
        return new BatteryReadInData(value);
    }

    @Override
    public Set<String> getHandledHeaders() {
        return handled;
    }
}
