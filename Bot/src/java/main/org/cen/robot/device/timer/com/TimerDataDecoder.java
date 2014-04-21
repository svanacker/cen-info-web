package org.cen.robot.device.timer.com;

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
import org.cen.robot.device.timer.TimerDevice;

/**
 * Input data decoder of the timer device.
 * 
 * @author Emmanuel ZURMELY
 */
//@formatter:off
@DeviceDataSignature(deviceName = TimerDevice.NAME, methods = {
		@DeviceMethodSignature(header = MatchFinishedInData.HEADER, methodName="endOfMatch", type = DeviceMethodType.OUTPUT, parameters = {}),
		@DeviceMethodSignature(header = MatchStartedInData.HEADER, methodName="startOfMatch", type = DeviceMethodType.OUTPUT, parameters = {}),
		@DeviceMethodSignature(header = RobotInitializedInData.HEADER, methodName="initMatch", type = DeviceMethodType.OUTPUT, parameters = { @DeviceParameter(length = 3,
				type = DeviceParameterType.UNSPECIFIED, name = "sequence", unit = "") }) })
//@formatter:on
public class TimerDataDecoder extends DefaultDecoder {

    private final static Set<String> handled = new HashSet<String>();

    private static final String INITIALIZATION_SEQUENCE = "xXyY";

    static {
        handled.add(MatchStartedInData.HEADER);
        handled.add(MatchFinishedInData.HEADER);
        handled.add(RobotInitializedInData.HEADER);
    }

    @Override
    public InData decode(String data) throws IllegalComDataException {
        if (data.substring(0, 1).equals(MatchStartedInData.HEADER)) {
            return new MatchStartedInData();
        } else if (data.substring(0, INITIALIZATION_SEQUENCE.length()).equals(INITIALIZATION_SEQUENCE)) {
            return new RobotInitializedInData();
        } else if (data.substring(0, 1).equals(MatchFinishedInData.HEADER)) {
            return new MatchFinishedInData();
        }
        throw new IllegalComDataException();
    }

    @Override
    public Set<String> getHandledHeaders() {
        return handled;
    }
}
