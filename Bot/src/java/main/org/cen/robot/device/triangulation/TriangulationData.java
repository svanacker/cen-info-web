package org.cen.robot.device.triangulation;

import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.triangulation.com.ReadTriangulationInData;
import org.cen.robot.services.IRobotServiceProvider;

/**
 * Wrapper object for incomming data.
 * 
 * @author Emmanuel ZURMELY
 */
public class TriangulationData {
	private final int[] angles;

	public TriangulationData(ReadTriangulationInData data) {
		super();
		angles = new int[3];
		angles[0] = data.getCurrentPosition();
		angles[1] = data.getLastPosition();
		angles[2] = data.getPreviousPosition();
	}

	public double[] getAngles(IRobotServiceProvider servicesProvider) {
		double[] d = new double[3];
		for (int i = 0; i < angles.length; i++) {
			d[i] = toAngle(angles[i], servicesProvider);
		}
		return d;
	}

	public double toAngle(int position, IRobotServiceProvider servicesProvider) {
		IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
		double maxPositions = (Double) handler.getProperty(TriangulationDevice.NAME, "positionCount");
		return (Math.PI * position) / maxPositions;
	}
}
