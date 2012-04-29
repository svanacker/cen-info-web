package org.cen.robot.device.collision.com;

import java.awt.geom.Point2D;
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
import org.cen.robot.device.collision.CollisionDetectionDevice;

@DeviceDataSignature(deviceName = CollisionDetectionDevice.NAME, methods = {
		@DeviceMethodSignature(header = CollisionReadInData.HEADER, type = DeviceMethodType.OUTPUT, parameters = {
				@DeviceParameter(name = "left", length = 4, type = DeviceParameterType.SIGNED, unit = "mm"),
				@DeviceParameter(name = "-", length = 1, type = DeviceParameterType.UNSPECIFIED, unit = ""),
				@DeviceParameter(name = "right", length = 4, type = DeviceParameterType.SIGNED, unit = "mm"),
				@DeviceParameter(name = "-", length = 1, type = DeviceParameterType.UNSPECIFIED, unit = ""),
				@DeviceParameter(name = "distance", length = 4, type = DeviceParameterType.SIGNED, unit = "mm"),
				@DeviceParameter(name = "-", length = 1, type = DeviceParameterType.UNSPECIFIED, unit = ""),
				@DeviceParameter(name = "angle", length = 4, type = DeviceParameterType.SIGNED, unit = "deg") }),
		@DeviceMethodSignature(header = OpponentPositionInData.HEADER, type = DeviceMethodType.OUTPUT, parameters = {
				@DeviceParameter(name = "x", length = 4, type = DeviceParameterType.SIGNED, unit = "mm"),
				@DeviceParameter(name = "y", length = 4, type = DeviceParameterType.SIGNED, unit = "mm") }) })
public class CollisionDataDecoder extends DefaultDecoder {

	final static Set<String> handled = new HashSet<String>();

	static {
		handled.add(String.valueOf(CollisionReadInData.HEADER));
		handled.add(String.valueOf(OpponentPositionInData.HEADER));
	}

	@Override
	public InData decode(String data) throws IllegalComDataException {
		String header = data.substring(0, 1);
		if (header.equals(OpponentPositionInData.HEADER)) {
			return decodeOpponentPosition(data);
		} else if (header.equals(CollisionReadInData.HEADER)) {
			return decodeCollisionData(data);
		} else {
			throw new IllegalComDataException();
		}
	}

	private InData decodeOpponentPosition(String data) {
		long x = ComDataUtils.parseShortHex(data.substring(1, 5));
		long y = ComDataUtils.parseShortHex(data.substring(5, 9));
		return new OpponentPositionInData(new Point2D.Double(x, y));
	}

	private InData decodeCollisionData(String data) {
		long leftPosition = ComDataUtils.parseShortHex(data.substring(1, 5));
		long rightPosition = ComDataUtils.parseShortHex(data.substring(6, 10));
		double distance = ComDataUtils.parseShortHex(data.substring(11, 15));
		double angle = Math.toRadians(ComDataUtils.parseShortHex(data.substring(16, 20)));
		return new CollisionReadInData(leftPosition, rightPosition, distance, angle);
	}

	@Override
	public Set<String> getHandledHeaders() {
		return handled;
	}
}
