package org.cen.robot.device.navigation.analysis;

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
import org.cen.robot.control.MotionInstructionData;
import org.cen.robot.control.MotionProfileType;
import org.cen.robot.control.PIDMotionType;
import org.cen.robot.control.PIDType;
import org.cen.robot.device.navigation.NavigationDevice;

//@formatter:off
@DeviceDataSignature(deviceName = NavigationDevice.NAME, methods = {
// MotionInstructionInData
        @DeviceMethodSignature(
                header = MotionInstructionInData.HEADER,
                methodName = "motionInstruction",
                type = DeviceMethodType.OUTPUT, parameters = {
                @DeviceParameter(name = "index", length = 1, type = DeviceParameterType.UNSIGNED, unit = ""),
                @DeviceParameter(name = "a", length = 2, type = DeviceParameterType.UNSIGNED, unit = ""),
                @DeviceParameter(name = "speed", length = 2, type = DeviceParameterType.UNSIGNED, unit = ""),
                @DeviceParameter(name = "speedMax", length = 2, type = DeviceParameterType.UNSIGNED, unit = ""),
                @DeviceParameter(name = "-", length = 1, type = DeviceParameterType.UNSPECIFIED, unit = ""),
                @DeviceParameter(name = "t1", length = 3, type = DeviceParameterType.UNSIGNED, unit = "pidTime"),
                @DeviceParameter(name = "t2", length = 3, type = DeviceParameterType.UNSIGNED, unit = "pidTime"),
                @DeviceParameter(name = "t3", length = 3, type = DeviceParameterType.UNSIGNED, unit = "pidTime"),
                @DeviceParameter(name = "-", length = 1, type = DeviceParameterType.UNSPECIFIED, unit = ""),
                @DeviceParameter(name = "p1", length = 5, type = DeviceParameterType.UNSIGNED, unit = "pulse"),
                @DeviceParameter(name = "p2", length = 5, type = DeviceParameterType.UNSIGNED, unit = "pulse"),
                @DeviceParameter(name = "-", length = 1, type = DeviceParameterType.UNSPECIFIED, unit = ""),
                @DeviceParameter(name = "profileType", length = 1, type = DeviceParameterType.UNSIGNED, unit = ""),
                @DeviceParameter(name = "motionType", length = 1, type = DeviceParameterType.UNSIGNED, unit = ""),
                @DeviceParameter(name = "pidType", length = 1, type = DeviceParameterType.UNSIGNED, unit = "") }),
        // Motion
        @DeviceMethodSignature(
                header = PIDMotionDataInData.HEADER,
                methodName="pidMotionDataInData",
                type = DeviceMethodType.OUTPUT,
                parameters = {
                @DeviceParameter(name = "index", length = 1, type = DeviceParameterType.UNSIGNED, unit = ""),
                @DeviceParameter(name = "pidTime", length = 3, type = DeviceParameterType.UNSIGNED, unit = ""),
                @DeviceParameter(name = "pidType", length = 1, type = DeviceParameterType.UNSIGNED, unit = ""),
                @DeviceParameter(name = "-", length = 1, type = DeviceParameterType.UNSPECIFIED, unit = ""),
                @DeviceParameter(name = "position", length = 5, type = DeviceParameterType.SIGNED, unit = ""),
                @DeviceParameter(name = "-", length = 1, type = DeviceParameterType.UNSPECIFIED, unit = ""),
                @DeviceParameter(name = "error", length = 4, type = DeviceParameterType.UNSIGNED, unit = "pulse"),
                @DeviceParameter(name = "-", length = 1, type = DeviceParameterType.UNSPECIFIED, unit = ""),
                @DeviceParameter(name = "u", length = 2, type = DeviceParameterType.SIGNED, unit = "power"),
                @DeviceParameter(name = "-", length = 1, type = DeviceParameterType.UNSPECIFIED, unit = ""),
                @DeviceParameter(name = "time", length = 4, type = DeviceParameterType.UNSIGNED, unit = "pidTime"),
                @DeviceParameter(name = "absDeltaPositionIntegral", length = 4, type = DeviceParameterType.UNSIGNED, unit = "pulse"),
                @DeviceParameter(name = "uIntegral", length = 4, type = DeviceParameterType.UNSIGNED, unit = "pulse") }) })
//@formatter:on
public class MotionAnalysisDecoder extends DefaultDecoder {

    public final static Set<String> handled = new HashSet<String>();

    static {
        handled.add(MotionInstructionInData.HEADER);
        handled.add(PIDMotionDataInData.HEADER);
    }

    @Override
    public Set<String> getHandledHeaders() {
        return handled;
    }

    @Override
    public InData decode(String data) throws IllegalComDataException {
        String header = data.substring(0, 1);
        if (header.equals(MotionInstructionInData.HEADER)) {
            checkLength(header, data);
            return decodeMotionInstructionInData(data);
        } else if (header.equals(PIDMotionDataInData.HEADER)) {
            checkLength(header, data);
            return decodeMotionDataInData(data);
        }
        return new UntypedInData(data);
    }

    /**
     * Parse simple Data like this 0083030-020100120-0100005000-123
     * 
     * @param data
     * @return
     */
    private InData decodeMotionInstructionInData(String data) {
        MotionInstructionData instructionData = new MotionInstructionData();
        MotionInstructionInData result = new MotionInstructionInData(instructionData);

        instructionData.setIndex((int) ComDataUtils.parseIntHex(data.substring(1, 2)));
        instructionData.setA(ComDataUtils.parseIntHex(data.substring(2, 4)));
        instructionData.setSpeed(ComDataUtils.parseIntHex(data.substring(4, 6)));
        instructionData.setSpeedMax(ComDataUtils.parseIntHex(data.substring(6, 8)));
        // -
        instructionData.setT1(ComDataUtils.parseIntHex(data.substring(9, 12)));
        instructionData.setT2(ComDataUtils.parseIntHex(data.substring(12, 15)));
        instructionData.setT3(ComDataUtils.parseIntHex(data.substring(15, 18)));
        // -
        instructionData.setP1(ComDataUtils.parseIntHex(data.substring(19, 24)));
        instructionData.setP2(ComDataUtils.parseIntHex(data.substring(24, 29)));
        // -
        int profileType = (int) ComDataUtils.parseIntHex(data.substring(30, 31));
        instructionData.setProfileType(MotionProfileType.values()[profileType]);
        int motionType = (int) ComDataUtils.parseIntHex(data.substring(31, 32));
        instructionData.setMotionType(PIDMotionType.values()[motionType]);
        int pidType = (int) ComDataUtils.parseIntHex(data.substring(32, 33));
        instructionData.setPidType(PIDType.values()[pidType]);

        return result;
    }

    /**
     * Parse simple PID Data like this 0101001-0002005678-40200050008000
     * 
     * @param data
     * @return
     */
    private InData decodeMotionDataInData(String data) {
        PIDMotionDataInData result = new PIDMotionDataInData();

        result.setIndex((int) ComDataUtils.parseIntHex(data.substring(1, 2)));
        result.setPidTime(ComDataUtils.parseIntHex(data.substring(2, 5)));
        result.setPidType(ComDataUtils.parseIntHex(data.substring(5, 6)));

        // -
        result.setPosition(ComDataUtils.parseInt5CharHex(data.substring(7, 12)));
        result.setErrorDataError(ComDataUtils.parseShortHex(data.substring(13, 17)));

        // -
        result.setU(ComDataUtils.parseIntHex(data.substring(18, 20)));
        // -
        result.setEndInfoTime(ComDataUtils.parseShortHex(data.substring(21, 25)));
        result.setEndInfoAbsDeltaPositionIntegral(ComDataUtils.parseShortHex(data.substring(25, 29)));
        result.setEndInfoUIntegral(ComDataUtils.parseShortHex(data.substring(29, 33)));

        return result;
    }

}
