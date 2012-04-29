package org.cen.simulRobot.device.beacon;

import org.cen.com.IComService;
import org.cen.com.in.InData;
import org.cen.com.in.InDataListener;
import org.cen.com.out.OutData;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.device.AbstractRobotDevice;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.RobotDeviceRequest;
import org.cen.simulRobot.device.beacon.com.BeaconSimulDataDecoder;
import org.cen.simulRobot.device.beacon.com.BeaconSimulReadInData;
import org.cen.simulRobot.device.beacon.com.BeaconSimulReadOutData;
import org.cen.simulRobot.device.com.AckSimulOutData;

public class BeaconSimulDevice extends AbstractRobotDevice implements InDataListener {
	public final static String NAME = "beacon";

	private RobotDeviceRequest request;

	/**
	 * Constructor.
	 */
	public BeaconSimulDevice() {
		super(NAME);
	}

	/**
	 * Constructor for a cup specific implementation.
	 * 
	 * @param name
	 *            the name of the device
	 */
	protected BeaconSimulDevice(String name) {
		super(name);
	}

	@Override
	public void debug(String debugAction) {

	}

	private BeaconSimulReadResult getResult(BeaconSimulReadInData data) {
		return new BeaconSimulReadResult(request);
	}

	@Override
	public void initialize(IRobotServiceProvider servicesProvider) {
		this.servicesProvider = servicesProvider;
		IComService comService = servicesProvider.getService(IComService.class);
		comService.getDecodingService().registerDecoder(new BeaconSimulDataDecoder());
		comService.addInDataListener(this);
	}

	@Override
	protected void internalHandleRequest(RobotDeviceRequest request) {
		this.request = request;

		if (request instanceof BeaconSimulReadRequest) {
			int ax = ((BeaconSimulReadRequest)request).getX();
			int ay = ((BeaconSimulReadRequest)request).getY();

			send(new BeaconSimulReadOutData(ax, ay));
		}
	}

	protected void notifyResult(BeaconSimulReadResult result) {
		IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
		handler.notifyListeners(this, result);
	}

	@Override
	public void onInData(InData data) {
		if (data instanceof BeaconSimulReadInData) {
			IComService comService = servicesProvider.getService(IComService.class);
			comService.writeOutData(new AckSimulOutData("u"));
			notifyResult(getResult((BeaconSimulReadInData) data));
		}
	}

	void send(OutData outData) {
		IComService comService = servicesProvider.getService(IComService.class);
		comService.writeOutData(outData);
	}
}
