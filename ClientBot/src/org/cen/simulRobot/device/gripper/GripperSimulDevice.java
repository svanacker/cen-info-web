package org.cen.simulRobot.device.gripper;

import org.cen.com.IComService;
import org.cen.com.in.InData;
import org.cen.com.in.InDataListener;
import org.cen.com.out.OutData;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.device.AbstractRobotDevice;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.RobotDeviceRequest;
import org.cen.simulRobot.device.com.AckSimulOutData;
import org.cen.simulRobot.device.gripper.com.DropSimulInData;
import org.cen.simulRobot.device.gripper.com.GripperSimulDataDecoder;
import org.cen.simulRobot.device.gripper.com.GripperSimulReadInData;
import org.cen.simulRobot.device.gripper.com.TakeSimulInData;
/**
 * 
 * @author Omar BENOUAMER
 *
 */
public class GripperSimulDevice extends AbstractRobotDevice implements InDataListener {
	public final static String NAME = "gripper";

	private RobotDeviceRequest request;

	/**
	 * Constructor.
	 */
	public GripperSimulDevice() {
		super(NAME);
	}

	/**
	 * Constructor for a cup specific implementation.
	 * 
	 * @param name
	 *            the name of the device
	 */
	protected GripperSimulDevice(String name) {
		super(name);
	}

	@Override
	public void debug(String debugAction) {
	}

	private GripperSimulReadResult getResult(GripperSimulReadInData data) {
		if (data instanceof TakeSimulInData){
			return new TakeSimulResult(request);
		}else if (data instanceof DropSimulInData){
			return new DropSimulResult(request);
		}
		return null;
	}

	@Override
	public void initialize(IRobotServiceProvider servicesProvider) {
		this.servicesProvider = servicesProvider;
		IComService comService = servicesProvider.getService(IComService.class);
		comService.getDecodingService().registerDecoder(new GripperSimulDataDecoder());
		comService.addInDataListener(this);
	}

	@Override
	protected void internalHandleRequest(RobotDeviceRequest request) {
	}

	protected void notifyResult(GripperSimulReadResult result) {
		IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
		handler.notifyListeners(this, result);
	}

	@Override
	public void onInData(InData data) {
		if (data instanceof GripperSimulReadInData) {
			IComService comService = servicesProvider.getService(IComService.class);
			comService.writeOutData(new AckSimulOutData("t"));
			notifyResult(getResult((GripperSimulReadInData) data));
		}
	}

	void send(OutData outData) {

		IComService comService = servicesProvider.getService(IComService.class);
		comService.writeOutData(outData);
	}
}
