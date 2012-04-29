package org.cen.simulRobot.device.lcd;

import org.cen.com.IComService;
import org.cen.com.in.InData;
import org.cen.com.in.InDataListener;
import org.cen.com.out.OutData;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.device.AbstractRobotDevice;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.RobotDeviceRequest;
import org.cen.simulRobot.device.com.AckSimulOutData;
import org.cen.simulRobot.device.lcd.com.LCDSimulDataDecoder;
import org.cen.simulRobot.device.lcd.com.LCDSimulInData;

public class LCDSimulDevice extends AbstractRobotDevice implements InDataListener {
	public final static String NAME = "lcd";

	private RobotDeviceRequest request;

	/**
	 * Constructor.
	 */
	public LCDSimulDevice() {
		super(NAME);
	}

	/**
	 * Constructor for a cup specific implementation.
	 * 
	 * @param name
	 *            the name of the device
	 */
	protected LCDSimulDevice(String name) {
		super(name);
	}

	@Override
	public void debug(String debugAction) {
	}

	private LCDSimulReadResult getResult(LCDSimulInData data) {
		return new LCDSimulReadResult(request);
	}

	@Override
	public void initialize(IRobotServiceProvider servicesProvider) {
		this.servicesProvider = servicesProvider;
		IComService comService = servicesProvider.getService(IComService.class);
		comService.getDecodingService().registerDecoder(new LCDSimulDataDecoder());
		comService.addInDataListener(this);
	}

	@Override
	protected void internalHandleRequest(RobotDeviceRequest request) {
	}

	protected void notifyResult(LCDSimulReadResult result) {
		IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
		handler.notifyListeners(this, result);
	}

	@Override
	public void onInData(InData data) {
		if (data instanceof LCDSimulInData) {
			IComService comService = servicesProvider.getService(IComService.class);
			comService.writeOutData(new AckSimulOutData("L"));
			notifyResult(getResult((LCDSimulInData) data));
		}
	}

	void send(OutData outData) {

		IComService comService = servicesProvider.getService(IComService.class);
		comService.writeOutData(outData);
	}
}
