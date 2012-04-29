package org.cen.simulRobot.device.vision;

import org.cen.com.IComService;
import org.cen.com.in.InData;
import org.cen.com.in.InDataListener;
import org.cen.com.out.OutData;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.device.AbstractRobotDevice;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.RobotDeviceRequest;
import org.cen.simulRobot.device.com.AckSimulOutData;
import org.cen.simulRobot.device.vision.com.VisionSimulDataDecoder;
import org.cen.simulRobot.device.vision.com.VisionSimulPositionOutData;
import org.cen.simulRobot.device.vision.com.VisionSimulReadInData;

public class VisionSimulDevice extends AbstractRobotDevice implements InDataListener {
	public final static String NAME = "vision";

	private RobotDeviceRequest request;

	/**
	 * Constructor.
	 */
	public VisionSimulDevice() {
		super(NAME);
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            the name of the device
	 */
	protected VisionSimulDevice(String name) {
		super(name);
	}

	@Override
	public void debug(String debugAction) {
	}

	private VisionSimulReadResult getResult(VisionSimulReadInData data) {
		return new VisionSimulReadResult(request);
	}

	@Override
	public void initialize(IRobotServiceProvider servicesProvider) {
		this.servicesProvider = servicesProvider;
		IComService comService = servicesProvider.getService(IComService.class);
		comService.getDecodingService().registerDecoder(new VisionSimulDataDecoder());
		comService.addInDataListener(this);
	}

	@Override
	protected void internalHandleRequest(RobotDeviceRequest request) {
		this.request = request;

		if (request instanceof VisionSimulPositionRequest) {
			int ax = ((VisionSimulPositionRequest)request).getX();
			int ay = ((VisionSimulPositionRequest)request).getY();

			send(new VisionSimulPositionOutData(ax, ay));
		}
	}

	protected void notifyResult(VisionSimulReadResult result) {
		IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
		handler.notifyListeners(this, result);
	}

	@Override
	public void onInData(InData data) {
		if (data instanceof VisionSimulReadInData) {
			IComService comService = servicesProvider.getService(IComService.class);
			comService.writeOutData(new AckSimulOutData("i"));
			notifyResult(getResult((VisionSimulReadInData) data));
		}
	}

	void send(OutData outData) {
		IComService comService = servicesProvider.getService(IComService.class);
		comService.writeOutData(outData);
	}
}
