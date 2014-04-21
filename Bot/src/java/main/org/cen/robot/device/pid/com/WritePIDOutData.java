package org.cen.robot.device.pid.com;

import org.cen.com.ComDataUtils;
import org.cen.com.documentation.DeviceDataSignature;
import org.cen.com.documentation.DeviceMethodSignature;
import org.cen.com.documentation.DeviceMethodType;
import org.cen.com.documentation.DeviceParameter;
import org.cen.com.documentation.DeviceParameterType;
import org.cen.com.out.OutData;
import org.cen.robot.control.PIDData;
import org.cen.robot.device.navigation.NavigationDevice;

/**
 * The encapsulation of the data which must be sent to change the PID.
 */
//@formatter:off
@DeviceDataSignature(deviceName = NavigationDevice.NAME, methods = { @DeviceMethodSignature(
		header = WritePIDOutData.HEADER,
		methodName = "writePID",
		type = DeviceMethodType.INPUT,
		parameters = { @DeviceParameter(name = "index", length = 2, type = DeviceParameterType.UNSIGNED, unit = ""),
				@DeviceParameter(name = "p", length = 2, type = DeviceParameterType.UNSIGNED, unit = ""),
				@DeviceParameter(name = "i", length = 2, type = DeviceParameterType.UNSIGNED, unit = ""),
				@DeviceParameter(name = "d", length = 2, type = DeviceParameterType.UNSIGNED, unit = ""),
				@DeviceParameter(name = "maxI", length = 2, type = DeviceParameterType.UNSIGNED, unit = "") }) })
//@formatter:on
public class WritePIDOutData extends OutData {

    /** The Header which is used by the message to change the PID. */
    public final static String HEADER = "p";

    /** Change all PID index in one Time. */
    protected final static int ALL_PID_INDEX = -1;

    protected PIDData data;

    private final int index;

    /**
     * Constructor
     * 
     * @param index
     *            the index of the PID
     * 
     * @param thetaData
     *            the data of the PID
     */
    public WritePIDOutData(int index, PIDData data) {
        super();
        this.index = index;
        this.data = data;
    }

    @Override
    public String getArguments() {
        return PIDEngineToData(data);
    }

    @Override
    public String getHeader() {
        return HEADER;
    }

    /**
     * Transform the PIDEngine data to a string with hexadecimal Value
     * 
     * @param engineData
     *            the data which must be converted
     * @return
     */
    protected String PIDEngineToData(PIDData engineData) {
        String indexString = ComDataUtils.format(index, 2);
        String pString = ComDataUtils.format(engineData.getP(), 2);
        String iString = ComDataUtils.format(engineData.getI(), 2);
        String dString = ComDataUtils.format(engineData.getD(), 2);
        String maxIString = ComDataUtils.format(engineData.getMaxI(), 2);

        return indexString + pString + iString + dString + maxIString;
    }
}
