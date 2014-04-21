package org.cen.robot.device.configuration.com;

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

/**
 * Class which is responsible of decoding data from the configuration which come
 * from the client.
 */
//@formatter:off
@DeviceDataSignature(deviceName = "configuration", methods = {
        @DeviceMethodSignature(
        header = ConfigurationReadInData.HEADER,
		methodName = "configuration",
        type = DeviceMethodType.OUTPUT, 
		parameters = { @DeviceParameter(length = 4, name = "value", type = DeviceParameterType.UNSIGNED, unit = "")
		})
})
//@formatter:on
public class ConfigurationDataDecoder extends DefaultDecoder {

    private static Set<String> handled = new HashSet<String>();

    static {
        handled.add(ConfigurationReadInData.HEADER);
    }

    @Override
    public InData decode(String data) throws IllegalComDataException {
        checkLength(ConfigurationReadInData.HEADER, data);
        int value = Integer.parseInt(data.substring(1, 5), 16);
        return new ConfigurationReadInData(value);
    }

    @Override
    public Set<String> getHandledHeaders() {
        return handled;
    }
}
