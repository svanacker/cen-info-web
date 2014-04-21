package org.cen.cup.cup2011.device.vision2011.com;

import java.util.HashSet;
import java.util.Set;

import org.cen.com.ComDataUtils;
import org.cen.com.DefaultDecoder;
import org.cen.com.IllegalComDataException;
import org.cen.com.documentation.DeviceDataSignature;
import org.cen.com.documentation.DeviceMethodSignature;
import org.cen.com.documentation.DeviceMethodType;
import org.cen.com.documentation.DeviceParameter;
import org.cen.com.documentation.DeviceParameterType;
import org.cen.com.in.InData;
import org.cen.com.in.UntypedInData;
import org.cen.cup.cup2011.device.vision2011.Vision2011Device;

//@formatter:off
@DeviceDataSignature(deviceName = Vision2011Device.NAME, methods = {
        @DeviceMethodSignature(
                header = PawnPositionInData.HEADER,
                methodName = "pawnPosition",
                type = DeviceMethodType.OUTPUT, parameters = {
				@DeviceParameter(name = "x", length = 4, type = DeviceParameterType.SIGNED, unit = "mm"),
				@DeviceParameter(name = "-", length = 1, type = DeviceParameterType.UNSPECIFIED, unit = ""),
				@DeviceParameter(name = "y", length = 4, type = DeviceParameterType.SIGNED, unit = "mm") }) })
//@formatter:on
public class Vision2011InDataDecoder extends DefaultDecoder {

    static final Set<String> HANDLED = new HashSet<String>();

    static {
        HANDLED.add(PawnPositionInData.HEADER);
    }

    @Override
    public InData decode(String data) throws IllegalComDataException {
        if (data.startsWith(PawnPositionInData.HEADER)) {
            int x = (int) ComDataUtils.parseShortHex(data.substring(1, 5));
            int y = (int) ComDataUtils.parseShortHex(data.substring(6, 10));
            return new PawnPositionInData(x, y);
        }
        return new UntypedInData(data);
    }

    @Override
    public int getDataLength(String header) {
        if (header.equals(PawnPositionInData.HEADER)) {
            return 9;
        }
        return 0;
    }

    @Override
    public Set<String> getHandledHeaders() {
        return HANDLED;
    }
}
