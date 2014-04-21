package org.cen.robot.device.servo.com;

import org.cen.com.ComDataUtils;
import org.cen.com.documentation.DeviceDataSignature;
import org.cen.com.documentation.DeviceMethodSignature;
import org.cen.com.documentation.DeviceMethodType;
import org.cen.com.documentation.DeviceParameter;
import org.cen.com.documentation.DeviceParameterType;
import org.cen.com.out.OutData;
import org.cen.robot.device.servo.ServoData;

/**
 * Encapsulation of a message to change the state of a servo.
 * 
 * @author svanacker
 */
@DeviceDataSignature(deviceName = "servo", methods = {
	    @DeviceMethodSignature(header = ServoOutData.HEADER, type = DeviceMethodType.INPUT, parameters = {
	            @DeviceParameter(name = "index", length = 2, type = DeviceParameterType.UNSIGNED, unit = ""),
	            @DeviceParameter(name = "speed", length = 2, type = DeviceParameterType.UNSIGNED, unit = ""),
	            @DeviceParameter(name = "value", length = 4, type = DeviceParameterType.UNSIGNED, unit = ""),
	    })
	})
public class ServoOutData extends OutData {
	protected static final String HEADER = "S";

	/** The id of the relay (between 0 and 20). */
	private int id;

	/** The value which must be applied for this servo. */
	private int value;

	/** The speed which must be used for the move. */
	private int speed;

	public ServoOutData(int id, int speed, int value) {
		super();
		this.id = id;
		this.speed = speed;
		this.value = value;
	}

	/**
	 * Build an encapsulation of outData from the object model
	 * 
	 * @param servoData
	 *            the object model which represents servo
	 */
	public ServoOutData(ServoData servoData) {
		super();
		this.id = servoData.getId() + 1;
		this.speed = servoData.getSpeed();
		this.value = servoData.getValue();
	}

	@Override
	public String getArguments() {
		String servoId = ComDataUtils.format(id, 2);
		String servoSpeed = ComDataUtils.format(speed, 2);
		String servoValue = ComDataUtils.format(value, 4);
		return servoId + servoSpeed + servoValue;
	}

	@Override
	public String getHeader() {
		return HEADER;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[id=" + id + ", speed=" + speed + ", value=" + value + "]";
	}
}
